package com.dark002.school_management.service;

import com.dark002.school_management.model.Teacher;
import com.dark002.school_management.model.Subject;
import com.dark002.school_management.repository.TeacherRepository;
import com.dark002.school_management.repository.SubjectRepository;
import com.dark002.school_management.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeacherService {
    @Autowired
    private TeacherRepository teachers;

    @Autowired
    private UserRepository users;

    @Autowired
    private SubjectRepository subjects;

    @Autowired
    private UtilsService utilsService;

    private void getExtraProperties(Teacher teacher) {
        List<Subject> subjectList = subjects.getAllByTeacher(teacher.getId());
        teacher.setAssignedSubjects(subjectList);
    }

    public List<Teacher> getAllTeachers() {
        return teachers.getAll();
    }

    public void addTeacher(Teacher teacher) {
        users.createUser(teacher.getEmail(), utilsService.convertDateToString(teacher.getDob()));
        teachers.createTeacher(teacher, teacher.getEmail());
    }

    public Teacher getTeacherById(String id) {
        Teacher teacher = teachers.getById(Integer.parseInt(id));
        getExtraProperties(teacher);
        return teacher;
    }

    public Teacher getTeacherById(int id) {
        return teachers.getById(id);
    }

    public Teacher getTeacherByEmail(String email) {
        Teacher teacher = teachers.getByEmail(email);
        getExtraProperties(teacher);
        return teacher;
    }

    public void deleteTeacher(Teacher teacher) {
        teachers.deleteTeacher(teacher.getId());
    }

    public void updateTeacher(Teacher teacher) {
        teachers.update(teacher);
    }
}
