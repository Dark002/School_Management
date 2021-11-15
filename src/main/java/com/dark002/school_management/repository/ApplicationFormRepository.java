package com.dark002.school_management.repository;

import com.dark002.school_management.model.ApplicationForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ApplicationFormRepository {
    @Autowired
    private JdbcTemplate template;

    public void createApplication(ApplicationForm application) {
        String sql = "INSERT INTO application_form (name, dob, email, fatherName, phoneNo, address, date, interestedStreams, sessionId) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        template.update(
                sql, application.getName(), application.getDob(), application.getEmail(),  application.getFatherName(),
                application.getPhoneNo(),  application.getAddress(), application.getDate(), application.getInterestedStreams(),
                application.getSessionId());
    }

    public List<ApplicationForm> getAllForSession(int sessionId) {
        String sql = "SELECT * FROM application_form WHERE sessionId = ?";
        return template.query(sql, new Object[] {sessionId}, new BeanPropertyRowMapper<>(ApplicationForm.class));
    }

    public ApplicationForm getById(int id) {
        String sql = "SELECT * FROM application_form WHERE id = ?";
        return template.queryForObject(sql, new Object[] {id}, new BeanPropertyRowMapper<>(ApplicationForm.class));
    }

    public void delete(int id) {
        String sql = "DELETE FROM application_form WHERE id = ?";
        template.update(sql, id);
    }
}
