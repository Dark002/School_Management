package com.dark002.school_management.service;

import com.dark002.school_management.model.ApplicationForm;
import com.dark002.school_management.model.Session;
import com.dark002.school_management.model.Student;
import com.dark002.school_management.repository.ApplicationFormRepository;
import com.dark002.school_management.repository.StudentRepository;
import com.dark002.school_management.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ApplicationFormService {
    @Autowired
    private ApplicationFormRepository applicationForms;

    @Autowired
    private StudentRepository students;

    @Autowired
    private UserRepository users;

    @Autowired
    private UtilsService utilsService;

    private void getExtraFields(ApplicationForm application) {
        try {
            Student student = students.getByApplicationId(application.getId());
            application.setStudent(student);
        } catch (Exception e) {}
    }

    public void createApplication(ApplicationForm applicationForm) {
        applicationForm.setDate(new Date());
        applicationForms.createApplication(applicationForm);
    }

    public List<ApplicationForm> getApplicationsForSession(Session session) {
        List<ApplicationForm> applications = applicationForms.getAllForSession(session.getId());
        for (int i = 0; i < applications.size(); i++) {
            getExtraFields(applications.get(i));
        }
        return applications;
    }

    public ApplicationForm getApplicationById(String id) {
        ApplicationForm application = applicationForms.getById(Integer.parseInt(id));
        getExtraFields(application);
        return application;
    }

    public void deleteApplication(ApplicationForm application) {
        applicationForms.delete(application.getId());
    }

    public void acceptApplication(ApplicationForm application, Student student) {
        student.setName(application.getName());
        student.setDob(application.getDob());
        student.setEmail(application.getEmail());
        student.setFatherName(application.getFatherName());
        student.setPhoneNo(application.getPhoneNo());
        student.setAddress(application.getAddress());
        student.setSessionId(application.getSessionId());
        student.setApplicationId(application.getId());
        
        users.createUser(
                student.getRollNo(),
                utilsService.convertDateToString(student.getDob()));
        student.setUsername(student.getRollNo());
        students.createStudent(student);
    }
}
