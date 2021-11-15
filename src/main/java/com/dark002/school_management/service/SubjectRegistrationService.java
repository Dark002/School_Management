package com.dark002.school_management.service;

import com.dark002.school_management.model.*;
import com.dark002.school_management.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubjectRegistrationService {
    @Autowired
    private SubjectRegistrationRepository classroomRegistrationSubjects;

    @Autowired
    private SubjectRepository subjects;

    @Autowired
    private ClassroomRegistrationRepository classroomRegistrations;

    @Autowired
    private ResultRepository results;

    @Autowired
    private ClassroomRepository classrooms;

    @Autowired
    private StudentRepository students;

    @Autowired
    private SessionRepository sessions;

    private void getExtraFields(SubjectRegistration classroomRegistrationSubject) {
        Subject subject = subjects.getById(classroomRegistrationSubject.getSubjectId());
        classroomRegistrationSubject.setSubject(subject);

        ClassroomRegistration classroomRegistration = classroomRegistrations.getById(classroomRegistrationSubject.getRegistrationId());
        Classroom classroom = classrooms.getById(classroomRegistration.getClassroomId());
        Student student = students.getByRollNo(classroomRegistration.getStudentRollNo());
        Session session = sessions.getById(student.getSessionId());
        student.setSession(session);
        classroomRegistration.setClassroom(classroom);
        classroomRegistration.setStudent(student);
        classroomRegistrationSubject.setRegistration(classroomRegistration);

        try {
            Result result = results.getByRegistration(classroomRegistrationSubject.getId());
            classroomRegistrationSubject.setResult(result);
        } catch (Exception e) {}
    }

    public List<SubjectRegistration> getAllBySubject(Subject subject) {
        List<SubjectRegistration> classroomRegistrationSubjectList = classroomRegistrationSubjects.getAllForSubject(subject.getId());

        for (int i = 0; i < classroomRegistrationSubjectList.size(); i++) {
            getExtraFields(classroomRegistrationSubjectList.get(i));
        }

        return classroomRegistrationSubjectList;
    }

    public SubjectRegistration getById(String id) {
        SubjectRegistration classroomRegistrationSubject = classroomRegistrationSubjects.getById(Integer.parseInt(id));
        getExtraFields(classroomRegistrationSubject);
        return classroomRegistrationSubject;
    }
}
