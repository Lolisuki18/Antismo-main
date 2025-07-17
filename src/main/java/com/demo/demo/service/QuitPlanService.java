package com.demo.demo.service;

import aj.org.objectweb.asm.commons.Remapper;

import com.demo.demo.entity.QuitPlan.QuitPlan;
import com.demo.demo.repository.QuitPlan.QuitPlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class QuitPlanService {

    @Autowired
    private QuitPlanRepository repository;
    @Autowired
    private UserService userService;

    public List<Object> getQuitPlansByUserId(Integer userId) {
       return List.of(
           userService.getUserNameAndMailById(userId),
           repository.findByUserId(userId)
       );


    }

    public List<QuitPlan> getQuitPlansByCoachId(Integer coachId) {
        return repository.findByCoachId(coachId);
    }

    public QuitPlan saveQuitPlan(QuitPlan quitPlan) {
        return repository.save(quitPlan);
    }

    public void deleteQuitPlan(Integer id) {
        repository.deleteById(id);
    }

    public List<QuitPlan> getAllQuitPlans() {
        return repository.findAll();
    }
}
