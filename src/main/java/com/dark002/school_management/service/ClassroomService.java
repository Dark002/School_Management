package com.dark002.school_management.service;

import com.dark002.school_management.model.*;
import com.dark002.school_management.repository.CourseStructureRepository;
import com.dark002.school_management.repository.ClassroomRegistrationRepository;
import com.dark002.school_management.repository.ClassroomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ClassroomService {
    @Autowired
    private ClassroomRepository classrooms;

    @Autowired
    private CourseStructureRepository courseStructures;

    @Autowired
    private ClassroomRegistrationRepository classroomRegistrations;

    public List<Classroom> getAllClassroomsInSession(Session session) {
        return classrooms.getAllInSession(session.getId());
    }

    public void createClassroom(Session session, Classroom classroom) {
        classroom.setSessionId(session.getId());
        classrooms.create(classroom);
    }

    public Classroom getClassroomById(String id) {
        return classrooms.getById(Integer.parseInt(id));
    }

    public Classroom getClassroomById(int id) {
        return classrooms.getById(id);
    }

    public void deleteClassroom(Classroom classroom) {
        classrooms.delete(classroom.getId());
    }

    public List<Classroom> getAllClassroomsForStudent(Student student) {
        List<Classroom> allClassrooms = classrooms.getAllInSession(student.getSessionId());
        List<Classroom> classroomList = new ArrayList<>();

        for (int i = 0; i < allClassrooms.size(); i++) {
            try {
                courseStructures.getByStreamAndClassroom(student.getStreamId(), allClassrooms.get(i).getId());
                Classroom classroom = allClassrooms.get(i);

                try {
                    ClassroomRegistration classroomRegistration = classroomRegistrations.getByStudentAndClassroom(
                            student.getRollNo(), classroom.getId());
                    classroom.setClassroomRegistration(classroomRegistration);
                } catch (Exception e) {}

                classroomList.add(classroom);
            } catch (Exception e) {}
        }

        return classroomList;
    }
}
