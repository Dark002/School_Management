package com.dark002.school_management.controller;

import com.dark002.school_management.model.ApplicationForm;
import com.dark002.school_management.model.Session;
import com.dark002.school_management.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
public class HomeController extends BaseController {
    @Autowired
    private StreamService streamService;

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private SessionService sessionService;

    @Autowired
    private ToastService toastService;

    @Autowired
    private ApplicationFormService applicationFormService;

    // Needed to automatically convert String date in form to Date object.
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"), true));
    }

    @GetMapping("/")
    public String home(Model model, HttpSession session) {
        addDefaultAttributes(model, session);

        model.addAttribute("streams", streamService.getAllStreams());
        model.addAttribute("teachers", teacherService.getAllTeachers());
        return  "site/home";
    }

    @GetMapping("/registration")
    public String registration(Model model, HttpSession session, RedirectAttributes attributes) {
        addDefaultAttributes(model, session);

        List<Session> sessionList = sessionService.getAllSessions();

        Boolean isRegistrationOpen = false;

        for (int i = 0; i < sessionList.size(); i++) {
            Session sessionObj = sessionList.get(i);
            if (sessionObj.getIsRegistrationOpen()) {
                isRegistrationOpen = true;
                break;
            }
        }

        if (!isRegistrationOpen) {
            toastService.redirectWithErrorToast(attributes, "Registrations closed.");
            return "redirect:/";
        }

        model.addAttribute("sessions", sessionList);
        model.addAttribute("application", new ApplicationForm());
        return  "site/registration";
    }

    @PostMapping("/registration")
    public String postRegistration(@ModelAttribute ApplicationForm application, Model model, HttpSession session, RedirectAttributes attributes) {
        applicationFormService.createApplication(application);
        toastService.redirectWithSuccessToast(attributes, "Your application was successfully submitted.");
        return  "redirect:/";
    }
}
