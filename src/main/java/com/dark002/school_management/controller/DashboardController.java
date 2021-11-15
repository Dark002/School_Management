package com.dark002.school_management.controller;

import com.dark002.school_management.form.OptionalSubjectsForm;
import com.dark002.school_management.model.*;
import com.dark002.school_management.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class DashboardController extends BaseController {
    @Autowired
    private TeacherService teacherService;

    @Autowired
    private ToastService toastService;

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private StreamService streamService;

    @Autowired
    private PayoutService payoutService;

    @Autowired
    private SessionService sessionService;

    @Autowired
    private ApplicationFormService applicationFormService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private ClassroomService classroomService;

    @Autowired
    private CourseStructureService courseStructureService;

    @Autowired
    private ClassroomRegistrationService classroomRegistrationService;

    @Autowired
    private FeeTransactionService feeTransactionService;

    @Autowired
    private SubjectRegistrationService classroomRegistrationSubjectService;

    @Autowired
    private ResultService resultService;

    @Autowired
    private UserService userService;

    // Needed to automatically convert String date in form to Date object.
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"), true));
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, HttpSession session) {
        if (!isAuthenticated(session)) {
            return "redirect:/";
        }

        addDefaultAttributes(model, session);
        return "dashboard/index";
    }

    @PostMapping("/dashboard/studentUpdate")
    public String studentUpdate(@ModelAttribute Student userProfile, Model model, HttpSession session, RedirectAttributes attributes) {
        if (!isAuthenticated(session)) {
            return "redirect:/";
        }

        addDefaultAttributes(model, session);

        String userRole = model.getAttribute("userRole").toString();
        if (!userRole.equals("student")) {
            return "redirect:/";
        }

        userProfile.setRollNo(model.getAttribute("username").toString());
        studentService.updateStudent(userProfile);
        toastService.redirectWithSuccessToast(attributes, "Profile updated successfully.");
        return "redirect:/dashboard";
    }

    @PostMapping("/dashboard/teacherUpdate")
    public String teacherUpdate(@ModelAttribute Teacher userProfile, Model model, HttpSession session, RedirectAttributes attributes) {
        if (!isAuthenticated(session)) {
            return "redirect:/";
        }

        addDefaultAttributes(model, session);

        String userRole = model.getAttribute("userRole").toString();
        if (!userRole.equals("teacher")) {
            return "redirect:/";
        }

        Teacher teacher = teacherService.getTeacherByEmail(model.getAttribute("username").toString());
        userProfile.setId(teacher.getId());
        userProfile.setEmail(teacher.getEmail());
        teacherService.updateTeacher(userProfile);
        toastService.redirectWithSuccessToast(attributes, "Profile updated successfully.");
        return "redirect:/dashboard";
    }

    @GetMapping("/dashboard/manage/teacher")
    public String manageTeacher(Model model, HttpSession session) {
        if (!isAuthenticated(session)) {
            return "redirect:/";
        }

        addDefaultAttributes(model, session);

        String userRole = model.getAttribute("userRole").toString();
        if (!userRole.equals("admin")) {
            return "redirect:/";
        }

        model.addAttribute("teachers", teacherService.getAllTeachers());
        return "dashboard/teachers";
    }

    @GetMapping("/dashboard/add/teacher")
    public String addTeacher(Model model, HttpSession session) {
        if (!isAuthenticated(session)) {
            return "redirect:/";
        }

        addDefaultAttributes(model, session);

        String userRole = model.getAttribute("userRole").toString();
        if (!userRole.equals("admin")) {
            return "redirect:/";
        }

        model.addAttribute("teacher", new Teacher());
        return "dashboard/addTeacher";
    }

    @PostMapping("/dashboard/add/teacher")
    public String postAddTeacher(@ModelAttribute Teacher teacher, Model model, HttpSession session, RedirectAttributes attributes) {
        if (!isAuthenticated(session)) {
            return "redirect:/";
        }

        addDefaultAttributes(model, session);

        String userRole = model.getAttribute("userRole").toString();
        if (!userRole.equals("admin")) {
            return "redirect:/";
        }

        try {
            teacherService.addTeacher(teacher);
            toastService.redirectWithSuccessToast(attributes, "Teacher added successfully.");
            return "redirect:/dashboard/manage/teacher";
        } catch (Exception e) {}

        toastService.displayErrorToast(model, "Teacher with same email exists already.");
        model.addAttribute("teacher", teacher);
        return "dashboard/addTeacher";
    }

    @GetMapping("/dashboard/manage/teacher/{teacherId}")
    public String manageTeacher(@PathVariable("teacherId") String teacherId, Model model, HttpSession session) {
        if (!isAuthenticated(session)) {
            return "redirect:/";
        }

        addDefaultAttributes(model, session);

        String userRole = model.getAttribute("userRole").toString();
        if (!userRole.equals("admin")) {
            return "redirect:/";
        }

        Teacher teacher = teacherService.getTeacherById(teacherId);
        model.addAttribute("teacher", teacher);
        model.addAttribute("payouts", payoutService.getAllPayoutsByTeacher(teacher));
        model.addAttribute("deleteUrl", "/dashboard/manage/teacher/" + teacherId + "/delete");
        return "dashboard/teacher";
    }

    @GetMapping("/dashboard/manage/teacher/{teacherId}/add/payout")
    public String addPayout(@PathVariable("teacherId") String teacherId, Model model, HttpSession session) {
        if (!isAuthenticated(session)) {
            return "redirect:/";
        }

        addDefaultAttributes(model, session);

        String userRole = model.getAttribute("userRole").toString();
        if (!userRole.equals("admin")) {
            return "redirect:/";
        }

        Teacher teacher = teacherService.getTeacherById(teacherId);
        model.addAttribute("teacher", teacher);
        model.addAttribute("payout", new Payout());
        return "dashboard/addPayout";
    }

    @PostMapping("/dashboard/manage/teacher/{teacherId}/add/payout")
    public String postAddPayout(@PathVariable("teacherId") String teacherId, @ModelAttribute Payout payout, Model model, HttpSession session, RedirectAttributes attributes) {
        if (!isAuthenticated(session)) {
            return "redirect:/";
        }

        addDefaultAttributes(model, session);

        String userRole = model.getAttribute("userRole").toString();
        if (!userRole.equals("admin")) {
            return "redirect:/";
        }

        Teacher teacher = teacherService.getTeacherById(teacherId);
        payout.setDate(new Date());
        payoutService.addPayout(payout, teacher);
        toastService.redirectWithSuccessToast(attributes, "Payout added successfully.");
        return "redirect:/dashboard/manage/teacher/" + teacherId;
    }

    @GetMapping("/dashboard/manage/teacher/{teacherId}/payout/{payoutId}/delete")
    public String deletePayout(@PathVariable("teacherId") String teacherId, @PathVariable("payoutId") String payoutId, Model model, HttpSession session, RedirectAttributes attributes) {
        if (!isAuthenticated(session)) {
            return "redirect:/";
        }

        addDefaultAttributes(model, session);

        String userRole = model.getAttribute("userRole").toString();
        if (!userRole.equals("admin")) {
            return "redirect:/";
        }

        Payout payout = payoutService.getPayoutById(payoutId);
        payoutService.deletePayout(payout);
        toastService.redirectWithSuccessToast(attributes, "Payout deleted successfully.");
        return "redirect:/dashboard/manage/teacher/" + teacherId;
    }

    @GetMapping("/dashboard/manage/teacher/{teacherId}/delete")
    public String deleteTeacher(@PathVariable("teacherId") String teacherId, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        if (!isAuthenticated(session)) {
            return "redirect:/";
        }

        addDefaultAttributes(model, session);

        String userRole = model.getAttribute("userRole").toString();
        if (!userRole.equals("admin")) {
            return "redirect:/";
        }

        Teacher teacher = teacherService.getTeacherById(teacherId);

        try {
            teacherService.deleteTeacher(teacher);
            toastService.redirectWithSuccessToast(redirectAttributes, "Teacher deleted successfully.");
            return "redirect:/dashboard/manage/teacher";
        } catch (Exception e) {}

        toastService.redirectWithErrorToast(redirectAttributes, "Unassign all the subjects assigned to this teacher first.");
        return "redirect:/dashboard/manage/teacher/" + teacherId;
    }

    @GetMapping("/dashboard/manage/subjects")
    public String manageSubjects(Model model, HttpSession session) {
        if (!isAuthenticated(session)) {
            return "redirect:/";
        }

        addDefaultAttributes(model, session);

        String userRole = model.getAttribute("userRole").toString();
        if (!userRole.equals("admin")) {
            return "redirect:/";
        }

        model.addAttribute("subjects", subjectService.getAllSubjects());
        return "dashboard/subjects";
    }

    @GetMapping("/dashboard/add/subject")
    public String addSubject(Model model, HttpSession session) {
        if (!isAuthenticated(session)) {
            return "redirect:/";
        }

        addDefaultAttributes(model, session);

        String userRole = model.getAttribute("userRole").toString();
        if (!userRole.equals("admin")) {
            return "redirect:/";
        }

        model.addAttribute("subject", new Subject());
        return "dashboard/addSubject";
    }

    @PostMapping("/dashboard/add/subject")
    public String postAddSubject(@ModelAttribute Subject subject, Model model, HttpSession session, RedirectAttributes attributes) {
        if (!isAuthenticated(session)) {
            return "redirect:/";
        }

        addDefaultAttributes(model, session);

        String userRole = model.getAttribute("userRole").toString();
        if (!userRole.equals("admin")) {
            return "redirect:/";
        }

        subjectService.createSubject(subject);
        toastService.redirectWithSuccessToast(attributes, "Subject added successfully.");
        return "redirect:/dashboard/manage/subjects";
    }

    @GetMapping("/dashboard/manage/subject/{subjectId}")
    public String manageSubject(@PathVariable("subjectId") String subjectId, Model model, HttpSession session) {
        if (!isAuthenticated(session)) {
            return "redirect:/";
        }

        addDefaultAttributes(model, session);

        String userRole = model.getAttribute("userRole").toString();
        if (!userRole.equals("admin")) {
            return "redirect:/";
        }

        Subject subject = subjectService.getSubjectById(subjectId);
        model.addAttribute("subject", subject);
        model.addAttribute("deleteUrl", "/dashboard/manage/subject/" + subjectId + "/delete");

        if (subject.getAssignedTo() == null) {
            model.addAttribute("teachers", teacherService.getAllTeachers());
            model.addAttribute("assignUrl", "/dashboard/manage/subject/" + subjectId + "/assign");
        } else {
            model.addAttribute("unassignUrl", "/dashboard/manage/subject/" + subjectId + "/unassign");
        }

        return "dashboard/subject";
    }

    @GetMapping("/dashboard/manage/subject/{subjectId}/delete")
    public String deleteSubject(@PathVariable("subjectId") String subjectId, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        if (!isAuthenticated(session)) {
            return "redirect:/";
        }

        addDefaultAttributes(model, session);

        String userRole = model.getAttribute("userRole").toString();
        if (!userRole.equals("admin")) {
            return "redirect:/";
        }

        Subject subject = subjectService.getSubjectById(subjectId);
        subjectService.deleteSubject(subject);
        toastService.redirectWithSuccessToast(redirectAttributes, "Subject deleted successfully.");
        return "redirect:/dashboard/manage/subjects";
    }

    @GetMapping("/dashboard/manage/subject/{subjectId}/unassign")
    public String unassignSubject(@PathVariable("subjectId") String subjectId, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        if (!isAuthenticated(session)) {
            return "redirect:/";
        }

        addDefaultAttributes(model, session);

        String userRole = model.getAttribute("userRole").toString();
        if (!userRole.equals("admin")) {
            return "redirect:/";
        }

        Subject subject = subjectService.getSubjectById(subjectId);
        subjectService.updateTeacher(subject, null);
        toastService.redirectWithSuccessToast(redirectAttributes, "Subject unassigned successfully.");
        return "redirect:/dashboard/manage/subject/" + subjectId;
    }

    @PostMapping("/dashboard/manage/subject/{subjectId}/assign")
    public String assignSubject(@PathVariable("subjectId") String subjectId, @ModelAttribute Subject subject, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        if (!isAuthenticated(session)) {
            return "redirect:/";
        }

        addDefaultAttributes(model, session);

        String userRole = model.getAttribute("userRole").toString();
        if (!userRole.equals("admin")) {
            return "redirect:/";
        }

        subjectService.updateTeacher(subject, subject.getTeacherId());
        toastService.redirectWithSuccessToast(redirectAttributes, "Subject assigned successfully.");
        return "redirect:/dashboard/manage/subject/" + subjectId;
    }

    @GetMapping("/dashboard/manage/streams")
    public String manageStreams(Model model, HttpSession session) {
        if (!isAuthenticated(session)) {
            return "redirect:/";
        }

        addDefaultAttributes(model, session);

        String userRole = model.getAttribute("userRole").toString();
        if (!userRole.equals("admin")) {
            return "redirect:/";
        }

        model.addAttribute("streams", streamService.getAllStreams());
        return "dashboard/streams";
    }

    @GetMapping("/dashboard/add/stream")
    public String addStream(Model model, HttpSession session) {
        if (!isAuthenticated(session)) {
            return "redirect:/";
        }

        addDefaultAttributes(model, session);

        String userRole = model.getAttribute("userRole").toString();
        if (!userRole.equals("admin")) {
            return "redirect:/";
        }

        model.addAttribute("stream", new Stream());
        return "dashboard/addStream";
    }

    @PostMapping("/dashboard/add/stream")
    public String postAddStream(@ModelAttribute Stream stream, Model model, HttpSession session, RedirectAttributes attributes) {
        if (!isAuthenticated(session)) {
            return "redirect:/";
        }

        addDefaultAttributes(model, session);

        String userRole = model.getAttribute("userRole").toString();
        if (!userRole.equals("admin")) {
            return "redirect:/";
        }

        streamService.createStream(stream);
        toastService.redirectWithSuccessToast(attributes, "Stream added successfully.");
        return "redirect:/dashboard/manage/streams";
    }

    @GetMapping("/dashboard/manage/stream/{streamId}/delete")
    public String deleteStream(@PathVariable("streamId") String streamId, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        if (!isAuthenticated(session)) {
            return "redirect:/";
        }

        addDefaultAttributes(model, session);

        String userRole = model.getAttribute("userRole").toString();
        if (!userRole.equals("admin")) {
            return "redirect:/";
        }

        Stream stream = streamService.getStreamById(streamId);
        streamService.deleteStream(stream);
        toastService.redirectWithSuccessToast(redirectAttributes, "Stream deleted successfully.");
        return "redirect:/dashboard/manage/streams";
    }

    @GetMapping("/dashboard/manage/sessions")
    public String manageSessions(Model model, HttpSession session) {
        if (!isAuthenticated(session)) {
            return "redirect:/";
        }

        addDefaultAttributes(model, session);

        String userRole = model.getAttribute("userRole").toString();
        if (!userRole.equals("admin")) {
            return "redirect:/";
        }

        model.addAttribute("sessions", sessionService.getAllSessions());
        return "dashboard/sessions";
    }

    @GetMapping("/dashboard/add/session")
    public String addSession(Model model, HttpSession session) {
        if (!isAuthenticated(session)) {
            return "redirect:/";
        }

        addDefaultAttributes(model, session);

        String userRole = model.getAttribute("userRole").toString();
        if (!userRole.equals("admin")) {
            return "redirect:/";
        }

        model.addAttribute("sessionObj", new Session());
        return "dashboard/addSession";
    }

    @PostMapping("/dashboard/add/session")
    public String postAddSession(@ModelAttribute Session sessionObj, Model model, HttpSession session, RedirectAttributes attributes) {
        if (!isAuthenticated(session)) {
            return "redirect:/";
        }

        addDefaultAttributes(model, session);

        String userRole = model.getAttribute("userRole").toString();
        if (!userRole.equals("admin")) {
            return "redirect:/";
        }

        sessionService.createSession(sessionObj);
        toastService.redirectWithSuccessToast(attributes, "Session added successfully.");
        return "redirect:/dashboard/manage/sessions";
    }

    @GetMapping("/dashboard/manage/session/{sessionId}")
    public String manageSession(@PathVariable("sessionId") String sessionId, Model model, HttpSession session) {
        if (!isAuthenticated(session)) {
            return "redirect:/";
        }

        addDefaultAttributes(model, session);

        String userRole = model.getAttribute("userRole").toString();
        if (!userRole.equals("admin")) {
            return "redirect:/";
        }

        Session sessionObj = sessionService.getSessionById(sessionId);
        model.addAttribute("sessionObj", sessionObj);
        model.addAttribute("applications", applicationFormService.getApplicationsForSession(sessionObj));
        model.addAttribute("students", studentService.getAllStudentsInSession(sessionObj));
        model.addAttribute("classrooms", classroomService.getAllClassroomsInSession(sessionObj));
        return "dashboard/session";
    }

    @GetMapping("/dashboard/manage/session/{sessionId}/application/{applicationId}")
    public String manageApplication(@PathVariable("sessionId") String sessionId, @PathVariable("applicationId") String applicationId, Model model, HttpSession session) {
        if (!isAuthenticated(session)) {
            return "redirect:/";
        }

        addDefaultAttributes(model, session);

        String userRole = model.getAttribute("userRole").toString();
        if (!userRole.equals("admin")) {
            return "redirect:/";
        }

        Session sessionObj = sessionService.getSessionById(sessionId);
        ApplicationForm applicationForm = applicationFormService.getApplicationById(applicationId);
        model.addAttribute("sessionObj", sessionObj);
        model.addAttribute("applicationObj", applicationForm);
        model.addAttribute("student", new Student());
        model.addAttribute("streams", streamService.getAllStreams());
        return "dashboard/application";
    }

    @PostMapping("/dashboard/manage/session/{sessionId}/application/{applicationId}/accept")
    public String acceptApplication(@ModelAttribute Student student, @PathVariable("sessionId") String sessionId, @PathVariable("applicationId") String applicationId, Model model, HttpSession session, RedirectAttributes attributes) {
        if (!isAuthenticated(session)) {
            return "redirect:/";
        }

        addDefaultAttributes(model, session);

        String userRole = model.getAttribute("userRole").toString();
        if (!userRole.equals("admin")) {
            return "redirect:/";
        }

        ApplicationForm applicationForm = applicationFormService.getApplicationById(applicationId);
        try {
            applicationFormService.acceptApplication(applicationForm, student);
            toastService.redirectWithSuccessToast(attributes, "Application accepted successfully.");
            return "redirect:/dashboard/manage/session/" + sessionId;
        } catch (Exception e) { }

        toastService.redirectWithErrorToast(attributes, "A student with the same roll no found.");
        return "redirect:/dashboard/manage/session/" + sessionId + "/application/" + applicationId;
    }

    @GetMapping("/dashboard/manage/session/{sessionId}/application/{applicationId}/delete")
    public String deleteApplication(@PathVariable("sessionId") String sessionId, @PathVariable("applicationId") String applicationId, Model model, HttpSession session, RedirectAttributes attributes) {
        if (!isAuthenticated(session)) {
            return "redirect:/";
        }

        addDefaultAttributes(model, session);

        String userRole = model.getAttribute("userRole").toString();
        if (!userRole.equals("admin")) {
            return "redirect:/";
        }

        ApplicationForm applicationForm = applicationFormService.getApplicationById(applicationId);
        applicationFormService.deleteApplication(applicationForm);
        toastService.redirectWithSuccessToast(attributes, "Application deleted successfully.");
        return "redirect:/dashboard/manage/session/" + sessionId;
    }

    @GetMapping("/dashboard/manage/session/{sessionId}/toggleCompletion")
    public String toggleCompleteSession(@PathVariable("sessionId") String sessionId, Model model, HttpSession session, RedirectAttributes attributes) {
        if (!isAuthenticated(session)) {
            return "redirect:/";
        }

        addDefaultAttributes(model, session);

        String userRole = model.getAttribute("userRole").toString();
        if (!userRole.equals("admin")) {
            return "redirect:/";
        }

        Session sessionObj = sessionService.getSessionById(sessionId);
        sessionService.toggleSessionCompletionStatus(sessionObj);
        toastService.redirectWithSuccessToast(attributes, "Session updated successfully.");
        return "redirect:/dashboard/manage/session/" + sessionId;
    }

    @GetMapping("/dashboard/manage/session/{sessionId}/toggleRegistrations")
    public String toggleRegistrationSession(@PathVariable("sessionId") String sessionId, Model model, HttpSession session, RedirectAttributes attributes) {
        if (!isAuthenticated(session)) {
            return "redirect:/";
        }

        addDefaultAttributes(model, session);

        String userRole = model.getAttribute("userRole").toString();
        if (!userRole.equals("admin")) {
            return "redirect:/";
        }

        Session sessionObj = sessionService.getSessionById(sessionId);
        sessionService.toggleSessionRegistrationsStatus(sessionObj);
        toastService.redirectWithSuccessToast(attributes, "Session updated successfully.");
        return "redirect:/dashboard/manage/session/" + sessionId;
    }

    @GetMapping("/dashboard/manage/session/{sessionId}/delete")
    public String deleteSession(@PathVariable("sessionId") String sessionId, Model model, HttpSession session, RedirectAttributes attributes) {
        if (!isAuthenticated(session)) {
            return "redirect:/";
        }

        addDefaultAttributes(model, session);

        String userRole = model.getAttribute("userRole").toString();
        if (!userRole.equals("admin")) {
            return "redirect:/";
        }

        Session sessionObj = sessionService.getSessionById(sessionId);
        sessionService.deleteSession(sessionObj);
        toastService.redirectWithSuccessToast(attributes, "Session deleted successfully.");
        return "redirect:/dashboard/manage/sessions";
    }

    @GetMapping("/dashboard/manage/session/{sessionId}/add/classroom")
    public String addClassroom(@PathVariable("sessionId") String sessionId, Model model, HttpSession session) {
        if (!isAuthenticated(session)) {
            return "redirect:/";
        }

        addDefaultAttributes(model, session);

        String userRole = model.getAttribute("userRole").toString();
        if (!userRole.equals("admin")) {
            return "redirect:/";
        }

        Session sessionObj = sessionService.getSessionById(sessionId);
        model.addAttribute("sessionObj", sessionObj);
        model.addAttribute("classroom", new Classroom());
        return "dashboard/addClassroom";
    }

    @PostMapping("/dashboard/manage/session/{sessionId}/add/classroom")
    public String postAddClassroom(@ModelAttribute Classroom classroom, @PathVariable("sessionId") String sessionId, Model model, HttpSession session, RedirectAttributes attributes) {
        if (!isAuthenticated(session)) {
            return "redirect:/";
        }

        addDefaultAttributes(model, session);

        String userRole = model.getAttribute("userRole").toString();
        if (!userRole.equals("admin")) {
            return "redirect:/";
        }

        Session sessionObj = sessionService.getSessionById(sessionId);
        classroomService.createClassroom(sessionObj, classroom);
        toastService.redirectWithSuccessToast(attributes, "Classroom added successfully.");
        return "redirect:/dashboard/manage/session/" + sessionId;
    }

    @GetMapping("/dashboard/manage/session/{sessionId}/classroom/{classroomId}")
    public String manageClassroom(@PathVariable("sessionId") String sessionId, @PathVariable("classroomId") String classroomId, Model model, HttpSession session) {
        if (!isAuthenticated(session)) {
            return "redirect:/";
        }

        addDefaultAttributes(model, session);

        String userRole = model.getAttribute("userRole").toString();
        if (!userRole.equals("admin")) {
            return "redirect:/";
        }

        Session sessionObj = sessionService.getSessionById(sessionId);
        Classroom classroom = classroomService.getClassroomById(classroomId);
        List<Stream> streams = streamService.getAllStreamsWithCourseStructure(classroom);
        model.addAttribute("sessionObj", sessionObj);
        model.addAttribute("classroom", classroom);
        model.addAttribute("streams", streams);
        return "dashboard/classroom";
    }

    @GetMapping("/dashboard/manage/session/{sessionId}/classroom/{classroomId}/add/structure/{streamId}")
    public String addStructure(@PathVariable("sessionId") String sessionId, @PathVariable("classroomId") String classroomId, @PathVariable("streamId") String streamId, Model model, HttpSession session, RedirectAttributes attributes) {
        if (!isAuthenticated(session)) {
            return "redirect:/";
        }

        addDefaultAttributes(model, session);

        String userRole = model.getAttribute("userRole").toString();
        if (!userRole.equals("admin")) {
            return "redirect:/";
        }

        Stream stream = streamService.getStreamById(streamId);
        Classroom classroom = classroomService.getClassroomById(classroomId);
        courseStructureService.createCourseStructure(stream, classroom);
        CourseStructure structure = courseStructureService.getStructureByStreamAndClassroom(stream, classroom);
        toastService.redirectWithSuccessToast(attributes, "Course structure created successfully.");
        return "redirect:/dashboard/manage/session/" + sessionId + "/classroom/" + classroomId + "/structure/" + structure.getId();
    }

    @GetMapping("/dashboard/manage/session/{sessionId}/classroom/{classroomId}/structure/{structureId}")
    public String manageStructure(@PathVariable("sessionId") String sessionId, @PathVariable("classroomId") String classroomId, @PathVariable("structureId") String structureId, Model model, HttpSession session) {
        if (!isAuthenticated(session)) {
            return "redirect:/";
        }

        addDefaultAttributes(model, session);

        String userRole = model.getAttribute("userRole").toString();
        if (!userRole.equals("admin")) {
            return "redirect:/";
        }

        Session sessionObj = sessionService.getSessionById(sessionId);
        CourseStructure structure = courseStructureService.getStructureById(structureId);
        Classroom classroom = classroomService.getClassroomById(classroomId);
        model.addAttribute("classroom", classroom);
        model.addAttribute("structure", structure);
        model.addAttribute("sessionObj", sessionObj);
        return "dashboard/courseStructure";
    }

    @GetMapping("/dashboard/manage/session/{sessionId}/classroom/{classroomId}/structure/{structureId}/delete")
    public String deleteStructure(@PathVariable("sessionId") String sessionId, @PathVariable("classroomId") String classroomId, @PathVariable("structureId") String structureId, Model model, HttpSession session, RedirectAttributes attributes) {
        if (!isAuthenticated(session)) {
            return "redirect:/";
        }

        addDefaultAttributes(model, session);

        String userRole = model.getAttribute("userRole").toString();
        if (!userRole.equals("admin")) {
            return "redirect:/";
        }

        CourseStructure structure = courseStructureService.getStructureById(structureId);
        courseStructureService.deleteStructure(structure);
        toastService.redirectWithSuccessToast(attributes, "Course structure deleted successfully.");
        return "redirect:/dashboard/manage/session/" + sessionId + "/classroom/" + classroomId;
    }

    @GetMapping("/dashboard/manage/session/{sessionId}/classroom/{classroomId}/structure/{structureId}/add/subject")
    public String addSubjectToCourseStructure(@PathVariable("sessionId") String sessionId, @PathVariable("classroomId") String classroomId, @PathVariable("structureId") String structureId, Model model, HttpSession session, RedirectAttributes attributes) {
        if (!isAuthenticated(session)) {
            return "redirect:/";
        }

        addDefaultAttributes(model, session);

        String userRole = model.getAttribute("userRole").toString();
        if (!userRole.equals("admin")) {
            return "redirect:/";
        }

        Session sessionObj = sessionService.getSessionById(sessionId);
        CourseStructure structure = courseStructureService.getStructureById(structureId);
        Classroom classroom = classroomService.getClassroomById(classroomId);
        SubjectCourseStructureRelation courseStructureSubject = new SubjectCourseStructureRelation();
        courseStructureSubject.setCourseStructureId(structure.getId());

        List<Subject> subjectList = subjectService.getAllSubjectsNotPresentInCourseStructure(structure);

        if (subjectList.size() == 0) {
            toastService.redirectWithErrorToast(attributes, "All subjects already present in this course structure.");
            return "redirect:/dashboard/manage/session/" + sessionId + "/classroom/" + classroomId + "/structure/" + structureId;
        }

        model.addAttribute("sessionObj", sessionObj);
        model.addAttribute("classroom", classroom);
        model.addAttribute("structure", structure);
        model.addAttribute("courseStructureSubject", courseStructureSubject);
        model.addAttribute("subjects", subjectList);
        return "dashboard/addSubjectToCourseStructure";
    }

    @PostMapping("/dashboard/manage/session/{sessionId}/classroom/{classroomId}/structure/{structureId}/add/subject")
    public String postAddSubjectToCourseStructure(@ModelAttribute SubjectCourseStructureRelation courseStructureSubject, @PathVariable("sessionId") String sessionId, @PathVariable("classroomId") String classroomId, @PathVariable("structureId") String structureId, Model model, HttpSession session, RedirectAttributes attributes) {
        if (!isAuthenticated(session)) {
            return "redirect:/";
        }

        addDefaultAttributes(model, session);

        String userRole = model.getAttribute("userRole").toString();
        if (!userRole.equals("admin")) {
            return "redirect:/";
        }

        courseStructureService.addSubjectToCourseStructure(courseStructureSubject);
        toastService.redirectWithSuccessToast(attributes, "Subject added to this course structure successfully.");
        return "redirect:/dashboard/manage/session/" + sessionId + "/classroom/" + classroomId + "/structure/" + structureId;
    }

    @GetMapping("/dashboard/manage/session/{sessionId}/classroom/{classroomId}/delete")
    public String deleteClassroom(@PathVariable("sessionId") String sessionId, @PathVariable("classroomId") String classroomId, Model model, HttpSession session, RedirectAttributes attributes) {
        if (!isAuthenticated(session)) {
            return "redirect:/";
        }

        addDefaultAttributes(model, session);

        String userRole = model.getAttribute("userRole").toString();
        if (!userRole.equals("admin")) {
            return "redirect:/";
        }

        Classroom classroom = classroomService.getClassroomById(classroomId);
        classroomService.deleteClassroom(classroom);
        toastService.redirectWithSuccessToast(attributes, "Classroom deleted successfully.");
        return "redirect:/dashboard/manage/session/" + sessionId;
    }

    @GetMapping("/dashboard/manage/student/{rollNo}")
    public String manageStudent(@PathVariable("rollNo") String rollNo, Model model, HttpSession session) {
        if (!isAuthenticated(session)) {
            return "redirect:/";
        }

        addDefaultAttributes(model, session);

        String userRole = model.getAttribute("userRole").toString();
        if (!userRole.equals("admin")) {
            return "redirect:/";
        }

        Student student = studentService.getStudentByRollNo(rollNo);
        List<ClassroomRegistration> classroomRegistrations = classroomRegistrationService.getAllClassroomRegistrationsByStudent(student);

        model.addAttribute("student", student);
        model.addAttribute("classroomRegistrations", classroomRegistrations);
        return "dashboard/student";
    }

    @GetMapping("/dashboard/manage/student/{rollNo}/registration/{registrationId}")
    public String manageStudent(@PathVariable("rollNo") String rollNo, @PathVariable("registrationId") String registrationId, Model model, HttpSession session) {
        if (!isAuthenticated(session)) {
            return "redirect:/";
        }

        addDefaultAttributes(model, session);

        String userRole = model.getAttribute("userRole").toString();
        if (!userRole.equals("admin")) {
            return "redirect:/";
        }

        Student student = studentService.getStudentByRollNo(rollNo);
        ClassroomRegistration classroomRegistration = classroomRegistrationService.getClassroomRegistrationById(registrationId);
        Classroom classroom = classroomService.getClassroomById(classroomRegistration.getClassroomId());
        List<FeeTransaction> feeTransactions = feeTransactionService.getAllFeeTransactionsByStudentForClassroom(student, classroom);

        model.addAttribute("student", student);
        model.addAttribute("classroom", classroom);
        model.addAttribute("classroomRegistration", classroomRegistration);
        model.addAttribute("feeTransactions", feeTransactions);
        return "dashboard/classroomRegistration";
    }

    @GetMapping("/dashboard/manage/student/{rollNo}/delete")
    public String deleteStudent(@PathVariable("rollNo") String rollNo, Model model, HttpSession session, RedirectAttributes attributes) {
        if (!isAuthenticated(session)) {
            return "redirect:/";
        }

        addDefaultAttributes(model, session);

        String userRole = model.getAttribute("userRole").toString();
        if (!userRole.equals("admin")) {
            return "redirect:/";
        }

        Student student = studentService.getStudentByRollNo(rollNo);
        studentService.deleteStudent(student);
        toastService.redirectWithSuccessToast(attributes, "Student deleted successfully.");
        return "redirect:/dashboard/manage/session/" + student.getSessionId();
    }

    @GetMapping("/dashboard/student/classrooms")
    public String studentClassrooms(Model model, HttpSession session) {
        if (!isAuthenticated(session)) {
            return "redirect:/";
        }

        addDefaultAttributes(model, session);

        String userRole = model.getAttribute("userRole").toString();
        if (!userRole.equals("student")) {
            return "redirect:/";
        }

        String rollNo = model.getAttribute("username").toString();
        Student student = studentService.getStudentByRollNo(rollNo);
        List<Classroom> classrooms = classroomService.getAllClassroomsForStudent(student);
        model.addAttribute("classrooms", classrooms);
        return "dashboard/studentClassrooms";
    }

    @GetMapping("/dashboard/student/classroom/{classroomId}/register")
    public String studentRegisterClassroom(@PathVariable("classroomId") String classroomId, Model model, HttpSession session) {
        if (!isAuthenticated(session)) {
            return "redirect:/";
        }

        addDefaultAttributes(model, session);

        String userRole = model.getAttribute("userRole").toString();
        if (!userRole.equals("student")) {
            return "redirect:/";
        }

        String rollNo = model.getAttribute("username").toString();
        Student student = studentService.getStudentByRollNo(rollNo);
        Classroom classroom = classroomService.getClassroomById(classroomId);
        CourseStructure courseStructure = courseStructureService.getStructureByStreamAndClassroom(student.getStream(), classroom);
        model.addAttribute("courseStructure", courseStructure);

        OptionalSubjectsForm optionalSubjectsForm = new OptionalSubjectsForm().buildForm(courseStructure.getOptionalSubjects());
        model.addAttribute("optionalSubjectsForm", optionalSubjectsForm);
        return "dashboard/studentClassroomRegister";
    }

    @PostMapping("/dashboard/student/classroom/{classroomId}/register")
    public String postStudentRegisterClassroom(@ModelAttribute OptionalSubjectsForm optionalSubjectsForm, @PathVariable("classroomId") String classroomId, Model model, HttpSession session, RedirectAttributes attributes) {
        if (!isAuthenticated(session)) {
            return "redirect:/";
        }

        addDefaultAttributes(model, session);

        String userRole = model.getAttribute("userRole").toString();
        if (!userRole.equals("student")) {
            return "redirect:/";
        }

        String rollNo = model.getAttribute("username").toString();
        Student student = studentService.getStudentByRollNo(rollNo);
        Classroom classroom = classroomService.getClassroomById(classroomId);
        CourseStructure courseStructure = courseStructureService.getStructureByStreamAndClassroom(student.getStream(), classroom);

        List<Subject> selectedOptionalSubjects = optionalSubjectsForm.getSelectedSubjects();
        List<Subject> compulsorySubjects = courseStructure.getCompulsorySubjects();

        List<Integer> subjectIds = new ArrayList<>();

        for (int i = 0; i < compulsorySubjects.size(); i++) {
            subjectIds.add(compulsorySubjects.get(i).getId());
        }

        for (int i = 0; i < selectedOptionalSubjects.size(); i++) {
            subjectIds.add(selectedOptionalSubjects.get(i).getId());
        }

        classroomRegistrationService.registerForClassroom(student, classroom, subjectIds);
        toastService.redirectWithSuccessToast(attributes, "Registered for classroom successfully.");
        return "redirect:/dashboard/student/classroom/" + classroomId;
    }

    @GetMapping("/dashboard/student/classroom/{classroomId}")
    public String getClassroom(@PathVariable("classroomId") String classroomId, Model model, HttpSession session) {
        if (!isAuthenticated(session)) {
            return "redirect:/";
        }

        addDefaultAttributes(model, session);

        String userRole = model.getAttribute("userRole").toString();
        if (!userRole.equals("student")) {
            return "redirect:/";
        }

        String rollNo = model.getAttribute("username").toString();
        Student student = studentService.getStudentByRollNo(rollNo);
        Classroom classroom = classroomService.getClassroomById(classroomId);
        ClassroomRegistration classroomRegistration = classroomRegistrationService.getClassroomRegistrationByStudentAndClassroom(student, classroom);
        List<FeeTransaction> feeTransactions = feeTransactionService.getAllFeeTransactionsByStudentForClassroom(student, classroom);

        model.addAttribute("classroom", classroom);
        model.addAttribute("classroomRegistration", classroomRegistration);
        model.addAttribute("feeTransactions", feeTransactions);
        return "dashboard/studentClassroom";
    }

    @GetMapping("/dashboard/student/classroom/{classroomId}/add/fee")
    public String addFee(@PathVariable("classroomId") String classroomId, Model model, HttpSession session) {
        if (!isAuthenticated(session)) {
            return "redirect:/";
        }

        addDefaultAttributes(model, session);

        String userRole = model.getAttribute("userRole").toString();
        if (!userRole.equals("student")) {
            return "redirect:/";
        }

        Classroom classroom = classroomService.getClassroomById(classroomId);

        model.addAttribute("classroom", classroom);
        model.addAttribute("feeTransaction", new FeeTransaction());
        return "dashboard/addFee";
    }

    @PostMapping("/dashboard/student/classroom/{classroomId}/add/fee")
    public String postAddFee(@ModelAttribute FeeTransaction feeTransaction, @PathVariable("classroomId") String classroomId, Model model, HttpSession session, RedirectAttributes attributes) {
        if (!isAuthenticated(session)) {
            return "redirect:/";
        }

        addDefaultAttributes(model, session);

        String userRole = model.getAttribute("userRole").toString();
        if (!userRole.equals("student")) {
            return "redirect:/";
        }

        String rollNo = model.getAttribute("username").toString();
        Student student = studentService.getStudentByRollNo(rollNo);
        Classroom classroom = classroomService.getClassroomById(classroomId);
        feeTransactionService.addFeeTransaction(student, classroom, feeTransaction);
        toastService.redirectWithSuccessToast(attributes, "Fee transaction added successfully.");
        return "redirect:/dashboard/student/classroom/" + classroomId;
    }

    @GetMapping("/dashboard/student/classroom/{classroomId}/fee/{feeId}/delete")
    public String deleteFee(@PathVariable("classroomId") String classroomId, @PathVariable("feeId") String feeId, Model model, HttpSession session, RedirectAttributes attributes) {
        if (!isAuthenticated(session)) {
            return "redirect:/";
        }

        addDefaultAttributes(model, session);

        String userRole = model.getAttribute("userRole").toString();
        if (!userRole.equals("student")) {
            return "redirect:/";
        }

        feeTransactionService.deleteFeeTransaction(feeId);
        toastService.redirectWithSuccessToast(attributes, "Fee transaction deleted successfully.");
        return "redirect:/dashboard/student/classroom/" + classroomId;
    }

    @GetMapping("/dashboard/student/subject/{subjectRegistrationId}")
    public String studentSubjectRegistration(@PathVariable("subjectRegistrationId") String subjectRegistrationId, Model model, HttpSession session) {
        if (!isAuthenticated(session)) {
            return "redirect:/";
        }

        addDefaultAttributes(model, session);

        String userRole = model.getAttribute("userRole").toString();
        if (!userRole.equals("student")) {
            return "redirect:/";
        }

        String rollNo = model.getAttribute("username").toString();
        Student student = studentService.getStudentByRollNo(rollNo);
        SubjectRegistration classroomRegistrationSubject = classroomRegistrationSubjectService.getById(subjectRegistrationId);

        try {
            Teacher teacher = teacherService.getTeacherById(classroomRegistrationSubject.getSubject().getTeacherId());
            model.addAttribute("teacher", teacher);
        } catch (Exception e) {
            model.addAttribute("teacher", null);
        }

        model.addAttribute("subjectRegistration", classroomRegistrationSubject);
        return "dashboard/studentSubjectRegistration";
    }

    @GetMapping("/dashboard/teacher/subjects")
    public String teacherSubjects(Model model, HttpSession session) {
        if (!isAuthenticated(session)) {
            return "redirect:/";
        }

        addDefaultAttributes(model, session);

        String userRole = model.getAttribute("userRole").toString();
        if (!userRole.equals("teacher")) {
            return "redirect:/";
        }

        String userEmail = model.getAttribute("username").toString();
        Teacher teacher = teacherService.getTeacherByEmail(userEmail);
        List<Subject> subjects = subjectService.getAllSubjectsByTeacher(teacher);

        model.addAttribute("subjects", subjects);
        return "dashboard/teacherSubjects";
    }

    @GetMapping("/dashboard/teacher/subject/{subjectId}")
    public String teacherSubject(@PathVariable("subjectId") String subjectId, Model model, HttpSession session) {
        if (!isAuthenticated(session)) {
            return "redirect:/";
        }

        addDefaultAttributes(model, session);

        String userRole = model.getAttribute("userRole").toString();
        if (!userRole.equals("teacher")) {
            return "redirect:/";
        }

        String userEmail = model.getAttribute("username").toString();
        Teacher teacher = teacherService.getTeacherByEmail(userEmail);
        Subject subject = subjectService.getSubjectById(subjectId);
        List<SubjectRegistration> classroomRegistrationSubjectList = classroomRegistrationSubjectService.getAllBySubject(subject);

        model.addAttribute("subject", subject);
        model.addAttribute("registrations", classroomRegistrationSubjectList);
        return "dashboard/teacherSubject";
    }

    @GetMapping("/dashboard/teacher/subject/{subjectId}/registration/{registrationId}")
    public String teacherSubjectRegistration(@PathVariable("subjectId") String subjectId, @PathVariable("registrationId") String registrationId, Model model, HttpSession session) {
        if (!isAuthenticated(session)) {
            return "redirect:/";
        }

        addDefaultAttributes(model, session);

        String userRole = model.getAttribute("userRole").toString();
        if (!userRole.equals("teacher")) {
            return "redirect:/";
        }

        String userEmail = model.getAttribute("username").toString();
        Teacher teacher = teacherService.getTeacherByEmail(userEmail);
        Subject subject = subjectService.getSubjectById(subjectId);
        SubjectRegistration classroomRegistrationSubject = classroomRegistrationSubjectService.getById(registrationId);

        model.addAttribute("subject", subject);
        model.addAttribute("registration", classroomRegistrationSubject);

        if (classroomRegistrationSubject.getResult() == null) {
            model.addAttribute("result", new Result());
        }

        return "dashboard/teacherSubjectRegistration";
    }

    @PostMapping("/dashboard/teacher/subject/{subjectId}/registration/{registrationId}/grade")
    public String teacherGradeSubjectRegistration(@ModelAttribute Result result, @PathVariable("subjectId") String subjectId, @PathVariable("registrationId") String registrationId, Model model, HttpSession session, RedirectAttributes attributes) {
        if (!isAuthenticated(session)) {
            return "redirect:/";
        }

        addDefaultAttributes(model, session);

        String userRole = model.getAttribute("userRole").toString();
        if (!userRole.equals("teacher")) {
            return "redirect:/";
        }

        resultService.createResult(result, registrationId);
        toastService.redirectWithSuccessToast(attributes, "Graded this student successfully.");
        return "redirect:/dashboard/teacher/subject/" + subjectId + "/registration/" + registrationId;
    }

    @GetMapping("/dashboard/teacher/payouts")
    public String teacherPayouts(Model model, HttpSession session) {
        if (!isAuthenticated(session)) {
            return "redirect:/";
        }

        addDefaultAttributes(model, session);

        String userRole = model.getAttribute("userRole").toString();
        if (!userRole.equals("teacher")) {
            return "redirect:/";
        }

        String userEmail = model.getAttribute("username").toString();
        Teacher teacher = teacherService.getTeacherByEmail(userEmail);
        List<Payout> payouts = payoutService.getAllPayoutsByTeacher(teacher);

        model.addAttribute("payouts", payouts);
        return "dashboard/teacherPayouts";
    }

    @GetMapping("/dashboard/changePassword")
    public String changePassword(Model model, HttpSession session) {
        if (!isAuthenticated(session)) {
            return "redirect:/";
        }

        addDefaultAttributes(model, session);
        model.addAttribute("userObj", new User());
        return "dashboard/changePassword";
    }

    @PostMapping("/dashboard/changePassword")
    public String postChangePassword(@ModelAttribute User userObj, Model model, HttpSession session, RedirectAttributes attributes) {
        if (!isAuthenticated(session)) {
            return "redirect:/";
        }

        addDefaultAttributes(model, session);
        userService.changePassword(model.getAttribute("username").toString(), userObj);
        toastService.redirectWithSuccessToast(attributes, "Password changed successfully.");
        return "redirect:/dashboard/changePassword";
    }
}
