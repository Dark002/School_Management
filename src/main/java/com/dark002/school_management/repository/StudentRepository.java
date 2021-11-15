package com.dark002.school_management.repository;

import com.dark002.school_management.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class StudentRepository {
    @Autowired
    private JdbcTemplate template;

    public Student getByApplicationId(int applicationId) {
        String sql = "SELECT * FROM student WHERE applicationId = ?";
        return template.queryForObject(sql, new Object[] {applicationId}, new BeanPropertyRowMapper<>(Student.class));
    }

    public void createStudent(Student student) {
        String sql = "INSERT INTO student (rollNo, name, dob, email, fatherName, phoneNo, address, sessionId, username, streamId, applicationId) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        template.update(sql, student.getRollNo(), student.getName(), student.getDob(), student.getEmail(),
                student.getFatherName(), student.getPhoneNo(), student.getAddress(), student.getSessionId(),
                student.getUsername(), student.getStreamId(), student.getApplicationId());
    }

    public List<Student> getAllInSession(int sessionId) {
        String sql = "SELECT * FROM student WHERE sessionId = ?";
        return template.query(sql, new Object[] {sessionId}, new BeanPropertyRowMapper<>(Student.class));
    }

    public Student getByRollNo(String rollNo) {
        String sql = "SELECT * FROM student WHERE rollNo = ?";
        return template.queryForObject(sql, new Object[] {rollNo}, new BeanPropertyRowMapper<>(Student.class));
    }

    public void delete(String rollNo) {
        String sql = "DELETE FROM student WHERE rollNo = ?";
        template.update(sql, rollNo);
    }

    public void update(Student student) {
        String sql = "UPDATE student SET name=?, dob=?, email=?, fatherName=?, phoneNo=?, address=? WHERE rollNo = ?";

        template.update(sql, student.getName(), student.getDob(), student.getEmail(), student.getFatherName(),
        		student.getPhoneNo(), student.getAddress(), student.getRollNo());
    }
}
