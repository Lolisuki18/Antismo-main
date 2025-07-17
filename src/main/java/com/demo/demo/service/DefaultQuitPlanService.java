package com.demo.demo.service;


import com.demo.demo.entity.DefaultQuitPlan.DefaultQuitPlan;
import com.demo.demo.repository.DefaultQuitPlan.DefaultQuitPlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DefaultQuitPlanService {

    @Autowired
    private DefaultQuitPlanRepository defaultQuitPlanRepository;

    public void saveDefaultQuitPlan(DefaultQuitPlan defaultQuitPlan) {
       defaultQuitPlanRepository.save(defaultQuitPlan);
    }

    public Optional<DefaultQuitPlan> getDefaultQuitPlanById(Integer id) {
        return defaultQuitPlanRepository.findById(id);
    }

    public Optional<DefaultQuitPlan> getDefaultQuitPlanByUserId(Integer userId) {
        return defaultQuitPlanRepository.findByUserId(userId);
    }

    public void deleteDefaultQuitPlanById(Integer id) {
        defaultQuitPlanRepository.deleteById(id);
    }


}
