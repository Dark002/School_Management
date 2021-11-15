package com.dark002.school_management.service;

import com.dark002.school_management.model.Session;
import com.dark002.school_management.model.Student;
import com.dark002.school_management.repository.StreamRepository;
import com.dark002.school_management.repository.ApplicationFormRepository;
import com.dark002.school_management.repository.SessionRepository;
import com.dark002.school_management.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {
    @Autowired
    private StudentRepository students;

    @Autowired
    private StreamRepository streams;

    @Autowired
    private SessionRepository sessions;

    @Autowired
    private ApplicationFormRepository applicationForms;

    private void getExtraFields(Student student) {
        student.setStream(streams.getById(student.getStreamId()));
        student.setSession(sessions.getById(student.getSessionId()));
    }

    public List<Student> getAllStudentsInSession(Session session) {
        List<Student> studentList = students.getAllInSession(session.getId());
        for (int i = 0; i < studentList.size(); i++) {
            getExtraFields(studentList.get(i));
        }
        return studentList;
    }

    public Student getStudentByRollNo(String rollNo) {
        Student student = students.getByRollNo(rollNo);
        getExtraFields(student);
        return student;
    }

    public void deleteStudent(Student student) {
        students.delete(student.getRollNo());
        applicationForms.delete(student.getApplicationId());
    }

    public void updateStudent(Student student) {
        students.update(student);
    }
}
