package com.demo.demo.service;

import com.demo.demo.dto.CoachInfoDTO;
import com.demo.demo.entity.Coach.Coach;
import com.demo.demo.repository.Coach.CoachRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class CoachService {

    @Autowired
    private  CoachRepository coachRepository;

    public Optional<Coach> getCoachById(Integer id) {
        return coachRepository.findById(id);
    }

    public Coach saveCoach(Coach coach) {
        return coachRepository.save(coach);
    }

    public void deleteCoach(Integer id) {
        coachRepository.deleteById(id);
    }

    public List<Coach> getAllCoachs() {
        return coachRepository.findAll();
    }

    public List<CoachInfoDTO> getAllCoachesInfo() {
        return coachRepository.findAllCoachesInfo();
    }
}
