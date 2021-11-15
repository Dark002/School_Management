package com.dark002.school_management.service;

import com.dark002.school_management.model.Session;
import com.dark002.school_management.repository.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SessionService {
    @Autowired
    private SessionRepository sessions;

    public List<Session> getAllSessions() {
        return sessions.getAllSessions();
    }

    public void createSession(Session session) {
        session.setIsComplete(false);
        session.setIsRegistrationOpen(false);
        sessions.create(session);
    }

    public Session getSessionById(String id) {
        return sessions.getById(Integer.parseInt(id));
    }

    public void toggleSessionCompletionStatus(Session session) {
        session.toggleIsComplete();
        sessions.update(session);
    }

    public void toggleSessionRegistrationsStatus(Session session) {
        session.toggleIsRegistrationOpen();
        sessions.update(session);
    }

    public void deleteSession(Session session) {
        sessions.delete(session.getId());
    }
}
