package com.dark002.school_management.repository;

import com.dark002.school_management.model.FeeTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class FeeTransactionRepository {
    @Autowired
    private JdbcTemplate template;

    public List<FeeTransaction> getAllByStudentForClassroom(String studentRollNo, int classroomId) {
        String sql = "SELECT * FROM fee_transaction WHERE studentRollNo = ? AND classroomId = ?";
        return template.query(sql, new Object[] {studentRollNo, classroomId}, new BeanPropertyRowMapper<>(FeeTransaction.class));
    }

    public void create(FeeTransaction feeTransaction) {
        String sql = "INSERT INTO fee_transaction (amount, date, remarks, transactionId, studentRollNo, classroomId) VALUES (?, ?, ?, ?, ?, ?)";
        template.update(sql, feeTransaction.getAmount(), feeTransaction.getDate(), feeTransaction.getRemarks(), 
        		feeTransaction.getTransactionId(), feeTransaction.getStudentRollNo(), feeTransaction.getClassroomId());
    }

    public void delete(int id) {
        String sql = "DELETE FROM fee_transaction WHERE id = ?";
        template.update(sql, id);
    }
}
