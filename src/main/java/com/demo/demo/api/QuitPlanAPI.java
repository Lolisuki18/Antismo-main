package com.demo.demo.api;

import com.demo.demo.entity.QuitPlan.QuitPlan;
import com.demo.demo.entity.User.User;
import com.demo.demo.service.QuitPlanService;
import com.demo.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/quit-plan")
@CrossOrigin(origins = "*")
public class QuitPlanAPI {

    @Autowired
    private QuitPlanService service;



    @PostMapping
    public ResponseEntity<QuitPlan> createQuitPlan(@RequestBody QuitPlan quitPlan) {
        quitPlan.setCreatedAt(LocalDateTime.now());
        QuitPlan savedQuitPlan = service.saveQuitPlan(quitPlan);
        System.out.println("Quit Plan created: " + savedQuitPlan);
        return ResponseEntity.ok(savedQuitPlan);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getQuitPlanByUserId(@PathVariable("userId") Integer userId) {
        List<Object> quitPlanAndUserInfo = service.getQuitPlansByUserId(userId);
        return ResponseEntity.ok(quitPlanAndUserInfo);
    }


    @GetMapping("/coach/{coachId}")
    public ResponseEntity<List<QuitPlan>> getQuitPlanByCoachId(@PathVariable("coachId") Integer coachId) {
        List<QuitPlan> quitPlans = service.getQuitPlansByCoachId(coachId);
        if (quitPlans.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(quitPlans);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuitPlan(@PathVariable("id") Integer id) {
        service.deleteQuitPlan(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public List<QuitPlan> getAllQuitPlans() {
        return service.getAllQuitPlans();
    }

}

