package com.dark002.school_management.service;

import com.dark002.school_management.model.*;
import com.dark002.school_management.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClassroomRegistrationService {
    @Autowired
    private ClassroomRegistrationRepository classroomRegistrations;

    @Autowired
    private SubjectRepository subjects;

    @Autowired
    private ClassroomRepository classrooms;

    @Autowired
    private SubjectRegistrationRepository subjectsRegistration;

    @Autowired
    private ResultRepository results;

    private void getExtraFields(ClassroomRegistration classroomRegistration) {
        List<SubjectRegistration> subjectList = subjectsRegistration
                .getSubjectsInClassroomRegistration(classroomRegistration.getId());
        for (int i = 0; i < subjectList.size(); i++) {
            Subject subject = subjects.getById(subjectList.get(i).getSubjectId());
            subjectList.get(i).setSubject(subject);

            try {
                Result result = results.getByRegistration(subjectList.get(i).getId());
                subjectList.get(i).setResult(result);
            } catch (Exception e) {}
        }

        Classroom classroom = classrooms.getById(classroomRegistration.getClassroomId());

        classroomRegistration.setSubjectRegistrations(subjectList);
        classroomRegistration.setClassroom(classroom);
    }

    public ClassroomRegistration getClassroomRegistrationByStudentAndClassroom(Student student, Classroom classroom) {
        ClassroomRegistration classroomRegistration = classroomRegistrations.getByStudentAndClassroom(student.getRollNo(), classroom.getId());
        getExtraFields(classroomRegistration);
        return classroomRegistration;
    }

    public ClassroomRegistration getClassroomRegistrationById(String id) {
        ClassroomRegistration classroomRegistration = classroomRegistrations.getById(Integer.parseInt(id));
        getExtraFields(classroomRegistration);
        return classroomRegistration;
    }

    public void registerForClassroom(Student student, Classroom classroom, List<Integer> subjectIds) {
        classroomRegistrations.register(student.getRollNo(), classroom.getId(), subjectIds);
    }

    public List<ClassroomRegistration> getAllClassroomRegistrationsByStudent(Student student) {
        List<ClassroomRegistration> classroomRegistrationList = classroomRegistrations.getAllByStudent(student.getRollNo());
        for (int i = 0; i < classroomRegistrationList.size(); i++) {
            getExtraFields(classroomRegistrationList.get(i));
        }
        return classroomRegistrationList;
    }
}
