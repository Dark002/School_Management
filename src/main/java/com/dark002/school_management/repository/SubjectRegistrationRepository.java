package com.dark002.school_management.repository;

import com.dark002.school_management.model.SubjectRegistration;
import com.dark002.school_management.model.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class SubjectRegistrationRepository {
    @Autowired
    private JdbcTemplate template;

    public List<SubjectRegistration> getAllForSubject(int subjectId) {
        String sql = "SELECT * FROM subject_registration_relation WHERE subjectId = ?";
        return template.query(sql, new Object[] {subjectId}, new BeanPropertyRowMapper<>(SubjectRegistration.class));
    }

    public SubjectRegistration getById(int id) {
        String sql = "SELECT * FROM subject_registration_relation WHERE id = ?";
        return template.queryForObject(sql, new Object[] {id}, new BeanPropertyRowMapper<>(SubjectRegistration.class));
    }

    public List<SubjectRegistration> getSubjectsInClassroomRegistration(int registrationId) {
        String sql = "SELECT * FROM subject_registration_relation WHERE registrationId = ?";
        return template.query(sql, new Object[] {registrationId}, new BeanPropertyRowMapper<>(SubjectRegistration.class));
    }
}
