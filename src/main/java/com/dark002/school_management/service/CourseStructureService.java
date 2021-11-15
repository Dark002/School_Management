package com.dark002.school_management.service;

import com.dark002.school_management.model.*;
import com.dark002.school_management.repository.CourseStructureRepository;
import com.dark002.school_management.repository.StreamRepository;
import com.dark002.school_management.repository.ClassroomRepository;
import com.dark002.school_management.repository.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseStructureService {
    @Autowired
    private CourseStructureRepository courseStructures;

    @Autowired
    private SubjectRepository subjects;

    @Autowired
    private StreamRepository streams;

    @Autowired
    private ClassroomRepository classrooms;

    private void getExtraFields(CourseStructure courseStructure) {
        List<Subject> optionalSubjects = subjects.getSubjectsInCourseStructure(courseStructure.getId(), true);
        List<Subject> compulsorySubjects = subjects.getSubjectsInCourseStructure(courseStructure.getId(), false);
        courseStructure.setCompulsorySubjects(compulsorySubjects);
        courseStructure.setOptionalSubjects(optionalSubjects);

        Stream stream = streams.getById(courseStructure.getStreamId());
        courseStructure.setStream(stream);

        Classroom classroom = classrooms.getById(courseStructure.getClassroomId());
        courseStructure.setClassroom(classroom);
    }

    public void createCourseStructure(Stream stream, Classroom classroom) {
        CourseStructure courseStructure = new CourseStructure();
        courseStructure.setStreamId(stream.getId());
        courseStructure.setClassroomId(classroom.getId());
        courseStructures.create(courseStructure);
    }

    public CourseStructure getStructureByStreamAndClassroom(Stream stream, Classroom classroom) {
        CourseStructure courseStructure = courseStructures.getByStreamAndClassroom(stream.getId(), classroom.getId());
        getExtraFields(courseStructure);
        return courseStructure;
    }

    public CourseStructure getStructureById(String id) {
        CourseStructure courseStructure = courseStructures.getById(Integer.parseInt(id));
        getExtraFields(courseStructure);
        return courseStructure;
    }

    public void deleteStructure(CourseStructure structure) {
        courseStructures.delete(structure.getId());
    }

    public void addSubjectToCourseStructure(SubjectCourseStructureRelation courseStructureSubject) {
        courseStructures.addSubject(courseStructureSubject);
    }
}
