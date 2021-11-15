package com.dark002.school_management.repository;

import com.dark002.school_management.model.ClassroomRegistration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ClassroomRegistrationRepository {
    @Autowired
    private JdbcTemplate template;

    public ClassroomRegistration getByStudentAndClassroom(String studentRollNo, int classroomId) {
        String sql = "SELECT * FROM classroom_registration WHERE studentRollNo = ? AND classroomId = ?";
        return template.queryForObject(sql, new Object[] {studentRollNo, classroomId}, new BeanPropertyRowMapper<>(ClassroomRegistration.class));
    }

    public ClassroomRegistration getById(int id) {
        String sql = "SELECT * FROM classroom_registration WHERE id = ?";
        return template.queryForObject(sql, new Object[] {id}, new BeanPropertyRowMapper<>(ClassroomRegistration.class));
    }

    public void register(String studentRollNo, int classroomId, List<Integer> subjectIds) {
        String sql = "INSERT INTO classroom_registration (studentRollNo, classroomId) VALUES (?, ?)";
        template.update(sql, studentRollNo, classroomId);

        ClassroomRegistration classroomRegistration = getByStudentAndClassroom(studentRollNo, classroomId);

        for (int i = 0; i < subjectIds.size(); i++) {
            sql = "INSERT INTO subject_registration_relation (registrationId, subjectId) VALUES (?, ?)";
            template.update(sql, classroomRegistration.getId(), subjectIds.get(i));
        }
    }

    public List<ClassroomRegistration> getAllByStudent(String studentRollNo) {
        String sql = "SELECT * FROM classroom_registration WHERE studentRollNo = ?";
        return template.query(sql, new Object[] {studentRollNo}, new BeanPropertyRowMapper<>(ClassroomRegistration.class));
    }
}
