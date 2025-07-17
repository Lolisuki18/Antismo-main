package com.demo.demo.service;

import com.demo.demo.entity.QuitPlanPhase.QuitPlanPhase;
import com.demo.demo.repository.QuitPlanPhase.QuitPlanPhaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuitPlanPhaseService {

    @Autowired
    private QuitPlanPhaseRepository repository;

    public QuitPlanPhase saveQuitPlanPhase(QuitPlanPhase quitPlanPhase) {
        return repository.save(quitPlanPhase);
    }

    public void deleteQuitPlanPhaseById(Integer id) {
        repository.deleteById(id);
    }

    public Iterable<QuitPlanPhase> getAllQuitPlanPhases() {
        return repository.findAll();
    }
}
