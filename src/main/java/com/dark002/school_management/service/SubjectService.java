package com.dark002.school_management.service;

import com.dark002.school_management.model.CourseStructure;
import com.dark002.school_management.model.Teacher;
import com.dark002.school_management.model.Subject;
import com.dark002.school_management.repository.TeacherRepository;
import com.dark002.school_management.repository.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SubjectService {
    @Autowired
    private SubjectRepository subjects;

    @Autowired
    private TeacherRepository teachers;

    private void getExtraProperties(Subject subject) {
        if (subject.getTeacherId() != null) {
            subject.setAssignedTo(teachers.getById(subject.getTeacherId()));
        }
    }

    public List<Subject> getAllSubjects() {
        List<Subject> subjectList = subjects.getAll();
        for (int i = 0; i < subjectList.size(); i++) {
            getExtraProperties(subjectList.get(i));
        }
        return subjectList;
    }

    public void createSubject(Subject subject) {
        subjects.createSubject(subject);
    }

    public Subject getSubjectById(String id) {
        Subject subject = subjects.getById(Integer.parseInt(id));
        getExtraProperties(subject);
        return subject;
    }

    public void deleteSubject(Subject subject) {
        subjects.deleteSubject(subject.getId());
    }

    public void updateTeacher(Subject subject, Integer teacherId) {
        subjects.updateTeacher(subject.getId(), teacherId);
    }

    public List<Subject> getAllSubjectsNotPresentInCourseStructure(CourseStructure courseStructure) {
        List<Subject> allSubjects = subjects.getAll();
        List<Subject> included = subjects.getSubjectsInCourseStructure(courseStructure.getId());
        List<Subject> subjectList = new ArrayList<>();

        for (int i = 0; i < allSubjects.size(); i++) {
            Subject subject = allSubjects.get(i);

            Boolean flag = true;

            for (int j = 0; j < included.size(); j++) {
                if (included.get(j).getId() == subject.getId()) {
                    flag = false;
                    break;
                }
            }

            if (flag) {
                subjectList.add(subject);
            }

        }
        return subjectList;
    }

    public List<Subject> getAllSubjectsByTeacher(Teacher teacher) {
        return subjects.getAllByTeacher(teacher.getId());
    }
}
