package com.demo.demo.api;

import com.demo.demo.entity.QuitPlanPhase.QuitPlanPhase;
import com.demo.demo.service.QuitPlanPhaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/quit-plan-phase")
@CrossOrigin(origins = "*")
public class QuitPlanPhaseAPI {

    @Autowired
    private QuitPlanPhaseService service;

    @GetMapping
    public Iterable<QuitPlanPhase> getAllQuitPlanPhases() {
        return service.getAllQuitPlanPhases();
    }

    @DeleteMapping("/{id}")
    public void deleteQuitPlanPhaseById(Integer id) {
        service.deleteQuitPlanPhaseById(id);
    }

    @PostMapping
    public QuitPlanPhase createQuitPlanPhase(@RequestBody QuitPlanPhase quitStagePhase) {
        System.out.println("Creating Quit Plan Phase: " + quitStagePhase);
        return service.saveQuitPlanPhase(quitStagePhase);
    }
    
}
