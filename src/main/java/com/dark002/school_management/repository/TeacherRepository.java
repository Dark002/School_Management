package com.dark002.school_management.repository;

import com.dark002.school_management.model.Teacher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TeacherRepository {
    @Autowired
    private JdbcTemplate template;

    public List<Teacher> getAll() {
        String sql = "SELECT * FROM teacher;";
        return template.query(sql, new BeanPropertyRowMapper<>(Teacher.class));
    }

    public void createTeacher(Teacher teacher, String username) {
        String sql = "INSERT INTO teacher (name, email, phoneNo, address, dob, username) VALUES (?, ?, ?, ?, ?, ?)";
        template.update(sql, teacher.getName(), teacher.getEmail(), teacher.getPhoneNo(),
                teacher.getAddress(), teacher.getDob(), username);
    }

    public Teacher getById(int id) {
        String sql = "SELECT * FROM teacher WHERE id = ?";
        return template.queryForObject(sql, new Object[] {id}, new BeanPropertyRowMapper<>(Teacher.class));
    }

    public void deleteTeacher(int id) {
        String sql = "DELETE FROM teacher WHERE id = ?";
        template.update(sql, id);
    }

    public Teacher getByEmail(String email) {
        String sql = "SELECT * FROM teacher WHERE email = ?";
        return template.queryForObject(sql, new Object[] {email}, new BeanPropertyRowMapper<>(Teacher.class));
    }

    public void update(Teacher teacher) {
        String sql = "UPDATE teacher SET name=?, email=?, phoneNo=?, address=?, dob=? WHERE id = ?";
        template.update(sql, teacher.getName(), teacher.getEmail(), teacher.getPhoneNo(),
                teacher.getAddress(), teacher.getDob(), teacher.getId());
    }
}
