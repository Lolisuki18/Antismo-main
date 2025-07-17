package com.demo.demo.service;

import com.demo.demo.dto.SessionInfoDTO;
import com.demo.demo.entity.Session.Session;
import com.demo.demo.repository.Session.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SessionService {
    @Autowired
    private SessionRepository sessionRepository;

    public void saveSession(Session session) {
        sessionRepository.save(session);
    }

    public Optional<Session> getSession(String id) {
        return sessionRepository.findById(id);
    }

    public List<Session> getAllSessions() {
        return sessionRepository.findAll();
    }

    public List<SessionInfoDTO> getSessionsByUserId(int userId) {
        return sessionRepository.findByUserId(userId);
    }

    public void deleteSession(String id) {
        sessionRepository.deleteById(id);
    }
}
