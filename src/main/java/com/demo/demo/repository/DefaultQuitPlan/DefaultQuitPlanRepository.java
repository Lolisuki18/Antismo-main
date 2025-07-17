package com.demo.demo.repository.DefaultQuitPlan;

import com.demo.demo.entity.DefaultQuitPlan.DefaultQuitPlan;

import com.demo.demo.service.DefaultQuitPlanService;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DefaultQuitPlanRepository extends JpaRepository<DefaultQuitPlan, Integer> {
    Optional<DefaultQuitPlan> findByUserId(Integer userId);
}
