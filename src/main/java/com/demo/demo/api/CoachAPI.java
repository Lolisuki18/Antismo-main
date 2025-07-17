package com.demo.demo.api;

import com.demo.demo.dto.CoachInfoDTO;
import com.demo.demo.entity.Coach.Coach;
import com.demo.demo.repository.Coach.CoachRepository;

import com.demo.demo.service.CoachService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/coachs")
@CrossOrigin(origins = "*")
public class CoachAPI {

    @Autowired
    private CoachService coachService;

    @Autowired
    private CoachRepository coachRepository;

    @GetMapping
    public Iterable<Coach> getAllCoachs() {
        return coachService.getAllCoachs();
    }

    @GetMapping("/{id}")
    public Coach getCoachById(@PathVariable("id") Integer id) {
        return coachService.getCoachById(id).orElse(null);
    }

    @GetMapping("/info")
    public List<CoachInfoDTO> getAllCoachesInfo() {
        return coachService.getAllCoachesInfo();
    }

    @GetMapping("/info/{id}")
    public CoachInfoDTO getCoachInfo(@PathVariable("id") Integer id) {
        return coachRepository.findCoachInfoById(id)
                .orElseThrow(() -> new RuntimeException("Coach not found with id: " + id));
    }

    @PostMapping
    public Coach createCoach(@RequestBody Coach coach) {
        return coachService.saveCoach(coach);
    }

    @DeleteMapping("/{id}")
    public void deleteCoach(@PathVariable("id") Integer id) {
        coachService.deleteCoach(id);
    }

}
