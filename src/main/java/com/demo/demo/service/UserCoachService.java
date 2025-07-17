package com.demo.demo.service;

import com.demo.demo.dto.AssignedUserCoachResponse;
import com.demo.demo.repository.User.UserCoachRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserCoachService {

    @Autowired
    private UserCoachRepository userCoachRepository;

    public Optional<List<AssignedUserCoachResponse>> findUsersAssigned(int coachId) {
        Optional<List<AssignedUserCoachResponse>> results = userCoachRepository.findByCoachId(coachId);
        return results;
    }
}
