package com.dark002.school_management.repository;

import com.dark002.school_management.model.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class StreamRepository {
    @Autowired
    private JdbcTemplate template;

    public List<Stream> getAll() {
        String sql = "SELECT * FROM stream;";
        return template.query(sql, new BeanPropertyRowMapper<>(Stream.class));
    }

    public void createStream(Stream stream) {
        String sql = "INSERT INTO stream (name) VALUES (?)";
        template.update(sql, stream.getName());
    }

    public void deleteStream(int id) {
        String sql = "DELETE FROM stream WHERE id = ?";
        template.update(sql, id);
    }

    public Stream getById(int id) {
        String sql = "SELECT * FROM stream WHERE id = ?";
        return template.queryForObject(sql, new Object[] {id}, new BeanPropertyRowMapper<>(Stream.class));
    }
}
