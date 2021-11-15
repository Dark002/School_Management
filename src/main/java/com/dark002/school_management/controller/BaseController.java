package com.dark002.school_management.controller;

import com.dark002.school_management.service.AuthenticationService;
import com.dark002.school_management.service.TeacherService;
import com.dark002.school_management.service.StudentService;
import com.dark002.school_management.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;

import javax.servlet.http.HttpSession;

abstract class BaseController {
    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private UserService userService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private TeacherService teacherService;

    public Boolean isAuthenticated(HttpSession session) {
        return authenticationService.isAuthenticated(session);
    }

    public void addDefaultAttributes(Model model, HttpSession session) {
        String currentUser = authenticationService.getCurrentUser(session);
        if (currentUser != null) {
            model.addAttribute("username", currentUser);
            model.addAttribute("userImageUrl", "https://ui-avatars.com/api/?name=" + currentUser);

            String userRole = userService.getRole(currentUser);
            model.addAttribute("userRole", userRole);

            if (userRole.equals("student")) {
                model.addAttribute("userProfile", studentService.getStudentByRollNo(currentUser));
            }

            if (userRole.equals("teacher")) {
                model.addAttribute("userProfile", teacherService.getTeacherByEmail(currentUser));
            }
        }
    }
}
