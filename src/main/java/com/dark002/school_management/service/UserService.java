package com.dark002.school_management.service;

import com.dark002.school_management.model.User;
import com.dark002.school_management.repository.TeacherRepository;
import com.dark002.school_management.repository.StudentRepository;
import com.dark002.school_management.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository users;

    @Autowired
    private TeacherRepository teachers;

    @Autowired
    private StudentRepository students;

    public String getRole(String username) {
        User user = users.getUser(username);

        if (user.getIsAdmin()) {
            return "admin";
        }

        try {
            teachers.getByEmail(username);
            return "teacher";
        } catch (Exception e) {}

        try {
            students.getByRollNo(username);
            return "student";
        } catch (Exception e) {}

        return "unknown";
    }

    public void changePassword(String username, User user) {
        user.setUsername(username);
        users.update(user);
    }
}
