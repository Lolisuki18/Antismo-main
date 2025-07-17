package com.demo.demo.repository.QuitPlan;


import com.demo.demo.entity.QuitPlan.QuitPlan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface  QuitPlanRepository  extends JpaRepository<QuitPlan, Integer> {
    List<QuitPlan> findByCoachId(Integer coachId);
    List<QuitPlan> findByUserId(Integer userId);
}
