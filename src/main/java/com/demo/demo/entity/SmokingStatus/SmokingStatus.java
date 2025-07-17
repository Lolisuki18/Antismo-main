package com.demo.demo.entity.SmokingStatus;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "smoking_statuses")
@Getter
@Setter
public class SmokingStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "fullname")
    private String fullName;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "cigarettes_per_day")
    private Long cigarettesPerDay;

    @Column(name = "frequency")
    private String frequency;

    @Column(name = "cost_per_pack")
    private Double costPerPack;

    @Column(name = "recorded_at")
    private LocalDateTime recordedAt;

    @Column(name = "note")
    private String note;

    @Column(name = "is_quitting")
    private Boolean isQuitting;

    @Column(name = "emotion")
    private String emotion;

    @Column(name = "location")
    private String location;

}
