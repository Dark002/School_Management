package com.dark002.school_management.repository;

import com.dark002.school_management.model.SubjectCourseStructureRelation;
import com.dark002.school_management.model.SubjectRegistration;
import com.dark002.school_management.model.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class SubjectRepository {
    @Autowired
    private JdbcTemplate template;

    public List<Subject> getAll() {
        String sql = "SELECT * FROM subject;";
        return template.query(sql, new BeanPropertyRowMapper<>(Subject.class));
    }

    public void createSubject(Subject subject) {
        String sql = "INSERT INTO subject (name, code) VALUES (?, ?)";
        template.update(sql, subject.getName(), subject.getCode());
    }

    public Subject getById(int id) {
        String sql = "SELECT * FROM subject WHERE id = ?";
        return template.queryForObject(sql, new Object[] {id}, new BeanPropertyRowMapper<>(Subject.class));
    }

    public void deleteSubject(int id) {
        String sql = "DELETE FROM subject WHERE id = ?";
        template.update(sql, id);
    }

    public void updateTeacher(int id, Integer teacherId) {
        String sql = "UPDATE subject SET teacherId=? WHERE id=?";
        template.update(sql, teacherId, id);
    }

    public List<Subject> getAllByTeacher(Integer teacherId) {
        String sql = "SELECT * FROM subject WHERE teacherId = ?";
        return template.query(sql, new Object[] {teacherId}, new BeanPropertyRowMapper<>(Subject.class));
    }

    public List<Subject> getSubjectsInCourseStructure(int courseStructureId, Boolean optional) {
    	String sql = "SELECT * FROM subject_course_structure_relation WHERE courseStructureId = ? AND optional = ?";
    	List<SubjectCourseStructureRelation> courseStructureSubjectList = template.query(sql, new Object[] {courseStructureId, optional}, new BeanPropertyRowMapper<>(SubjectCourseStructureRelation.class));
        List<Subject> subjectList = new ArrayList<>();
        for (int i = 0; i < courseStructureSubjectList.size(); i++) {
            subjectList.add(getById(courseStructureSubjectList.get(i).getSubjectId()));
        }
        return subjectList;
    }

    public List<Subject> getSubjectsInCourseStructure(int courseStructureId) {
        String sql = "SELECT * FROM subject_course_structure_relation WHERE courseStructureId = ?";
        List<SubjectCourseStructureRelation> courseStructureSubjectList = template.query(sql, new Object[] {courseStructureId}, new BeanPropertyRowMapper<>(SubjectCourseStructureRelation.class));
        List<Subject> subjectList = new ArrayList<>();
        for (int i = 0; i < courseStructureSubjectList.size(); i++) {
            subjectList.add(getById(courseStructureSubjectList.get(i).getSubjectId()));
        }
        return subjectList;
    }
}
