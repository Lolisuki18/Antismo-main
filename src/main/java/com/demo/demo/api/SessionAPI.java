package com.demo.demo.api;

import com.demo.demo.dto.GoogleEventDTO;
import com.demo.demo.dto.SessionInfoDTO;
import com.demo.demo.entity.Session.Session;
import com.demo.demo.entity.User.User;
import com.demo.demo.repository.Session.SessionRepository;
import com.demo.demo.repository.User.UserRepository;
import com.demo.demo.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/sessions")
public class SessionAPI {

    @Autowired
    private SessionService sessionService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SessionRepository sessionRepository;

    @GetMapping("/{userId}")
    public ResponseEntity<?> getSessionsByUserId(@PathVariable("userId") int id) {
        List<SessionInfoDTO> sessions = sessionService.getSessionsByUserId(id);
        if (sessions.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(sessions);
    }

    @GetMapping("/")
    public ResponseEntity<List<Session>> getAllSessions() {
      List<Session> sessions = sessionService.getAllSessions();
      return ResponseEntity.ok(sessions);
    }

    @PostMapping("/")
    public ResponseEntity<Session> createSession(@RequestBody Session session) {
        sessionService.saveSession(session);
        return ResponseEntity.ok(session);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSession(@PathVariable("id") String id) {
        Optional<Session> session = sessionService.getSession(id);
        if (session.isPresent()) {
            sessionService.deleteSession(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/sync")
    public ResponseEntity<?> syncCalendarEvents(@RequestBody List<GoogleEventDTO> events) {
        int userId = events.get(0).getUserId();
        for (GoogleEventDTO event : events) {
            boolean exists = sessionRepository.existsById((event.getId()));
            System.out.println(exists);
            User coach = userRepository.findUserByEmail(event.getCreator()) //getting coach by email
                    .orElseThrow(() -> new RuntimeException("Coach not found"));
            if (!exists) {
                Session s = new Session();
                s.setId(event.getId());
                s.setUserId(userId);
                s.setCoachId(coach.getId());
                s.setStartTime(event.getStart().toLocalDateTime());
                s.setEndTime(event.getEnd().toLocalDateTime());
                sessionRepository.save(s);
            }
        }

        return ResponseEntity.ok("Synced successfully");
    }

}
