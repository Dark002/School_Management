package com.dark002.school_management.repository;

import com.dark002.school_management.model.CourseStructure;
import com.dark002.school_management.model.SubjectCourseStructureRelation;
import com.dark002.school_management.model.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CourseStructureRepository {
    @Autowired
    private JdbcTemplate template;

    public CourseStructure getByStreamAndClassroom(int streamId, int classroomId) {
        String sql = "SELECT * FROM course_structure WHERE streamId = ? AND classroomId = ?";
        return template.queryForObject(sql, new Object[] {streamId, classroomId}, new BeanPropertyRowMapper<>(CourseStructure.class));
    }

    public void create(CourseStructure courseStructure) {
        String sql = "INSERT INTO course_structure (streamId, classroomId) VALUES (?, ?)";
        template.update(sql, courseStructure.getStreamId(), courseStructure.getClassroomId());
    }

    public CourseStructure getById(int id) {
        String sql = "SELECT * FROM course_structure WHERE id = ?";
        return template.queryForObject(sql, new Object[] {id}, new BeanPropertyRowMapper<>(CourseStructure.class));
    }

    public void delete(int id) {
        String sql = "DELETE FROM course_structure WHERE id = ?";
        template.update(sql, id);
    }

    public void addSubject(SubjectCourseStructureRelation courseStructureSubject) {
        String sql = "INSERT INTO subject_course_structure_relation (subjectId, courseStructureId, optional) VALUES (?, ?, ?)";
        template.update(sql, courseStructureSubject.getSubjectId(), courseStructureSubject.getCourseStructureId(), courseStructureSubject.getOptional());
    }
}
