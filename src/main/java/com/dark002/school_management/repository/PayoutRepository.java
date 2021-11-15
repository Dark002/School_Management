package com.dark002.school_management.repository;

import com.dark002.school_management.model.Payout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PayoutRepository {
    @Autowired
    private JdbcTemplate template;

    public List<Payout> getAllByTeacher(int teacherId) {
        String sql = "SELECT * FROM payout WHERE teacherId = ?";
        return template.query(sql, new Object[] {teacherId}, new BeanPropertyRowMapper<>(Payout.class));
    }

    public void addPayout(Payout payout) {
        String sql = "INSERT INTO payout (date, amount, remarks, transactionId, teacherId) VALUES (?, ?, ?, ?, ?)";
        template.update(sql, payout.getDate(), payout.getAmount(), payout.getRemarks(), payout.getTransactionId(), payout.getTeacherId());
    }

    public Payout getById(int id) {
        String sql = "SELECT * FROM payout WHERE id = ?";
        return template.queryForObject(sql, new Object[] {id}, new BeanPropertyRowMapper<>(Payout.class));
    }

    public void deletePayout(int id) {
        String sql = "DELETE FROM payout WHERE id = ?";
        template.update(sql, id);
    }
}
