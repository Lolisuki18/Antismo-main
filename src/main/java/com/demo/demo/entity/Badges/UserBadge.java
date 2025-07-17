package com.demo.demo.entity.Badges;



import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "UserBadges")
@Getter
@Setter
public class UserBadge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_id")
    private Integer userId; // Tạm dùng Integer, có User thì dùng ManyToOne

    @ManyToOne
    @JoinColumn(name = "badge_id")
    private Badges badge;

    @Column(name = "earned_at")
    private LocalDateTime earnedAt;

    @Column(name = "shared")
    private Boolean shared;
}
