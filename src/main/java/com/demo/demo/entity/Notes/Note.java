package com.demo.demo.entity.Notes;



import com.demo.demo.entity.QuitPlan.QuitPlan;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Entity
@Table(name = "notes")
@Getter
@Setter
public class Note {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;


    @Column(name = "plan_id")
    private Integer planId;  // Tên entity bảng kế hoạch
}

