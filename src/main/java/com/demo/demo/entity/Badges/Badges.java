package com.demo.demo.entity.Badges;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Entity
@Table(name = "Badges")
@Getter
@Setter
public class Badges {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "title")
    private String title;

    @Column(name = "create_at")
    private LocalDateTime createdAt;

    @Column(name = "description")
    private String description;

    @Column(name = "condition")
    private String condition;

    @Column(name = "badge_type", length = 500)
    private String badgetype;

    @Column(name = "badge_image", length = 500)
    private String badgeImage;

}


