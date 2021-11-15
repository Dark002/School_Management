package com.dark002.school_management.repository;

import com.dark002.school_management.model.Classroom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ClassroomRepository {
    @Autowired
    private JdbcTemplate template;

    public List<Classroom> getAllInSession(int sessionId) {
        String sql = "SELECT * FROM classroom WHERE sessionId = ?";
        return template.query(sql, new Object[] {sessionId}, new BeanPropertyRowMapper<>(Classroom.class));
    }

    public void create(Classroom classroom) {
        String sql = "INSERT INTO classroom (name, fee, sessionId) VALUES (?, ?, ?)";
        template.update(sql, classroom.getName(), classroom.getFee(), classroom.getSessionId());
    }

    public Classroom getById(int id) {
        String sql = "SELECT * FROM classroom WHERE id = ?";
        return template.queryForObject(sql, new Object[] {id}, new BeanPropertyRowMapper<>(Classroom.class));
    }

    public void delete(int id) {
        String sql = "DELETE FROM classroom WHERE id = ?";
        template.update(sql, id);
    }
}
