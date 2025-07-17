package com.demo.demo.entity.QuitPlanPhase;




import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "quit_stage_phases")
@Getter
@Setter
public class QuitPlanPhase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "phase_id")
    private Integer phaseId;

    @Column(name = "plan_id", nullable = false)
    private Integer planId;

    @Column(name = "phase_order", nullable = false)
    private Integer phaseOrder;

    @Column(nullable = false)
    private Integer duration;

    @Column(name = "limit_per_day", nullable = false)
    private Integer limitPerDay;

    @Override
    public String toString() {
        return "QuitPlanPhase: " + phaseId + ", " + planId + ", " + phaseOrder + ", " + duration + ", " + limitPerDay;
    }
}
