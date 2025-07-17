package com.demo.demo.entity.DefaultQuitPlan;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Entity
@Table(name = "default_quit_plan")
@Getter
@Setter
public class DefaultQuitPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "target_date")
    private LocalDate targetDate;

    @Column(name = "quit_reason")
    private String quitReason;

    @Column(name = "money_target")
    private Integer moneyTarget;

    @Column(name = "note")
    private String note;
}
