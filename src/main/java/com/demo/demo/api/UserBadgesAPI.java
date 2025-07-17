package com.demo.demo.api;

import com.demo.demo.dto.UserBadgeDTO;
import com.demo.demo.entity.Badges.Badges;
import com.demo.demo.entity.Badges.UserBadge;
import com.demo.demo.service.UserBadgeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user-badges")
public class UserBadgesAPI {
    @Autowired
    private UserBadgeService userBadgeService;

//    @PostMapping
//    public UserBadge create(@RequestBody UserBadge userBadge) {
//        return userBadgeService.save(userBadge);
//    }

    @GetMapping("/user/{userId}")
    public List<UserBadge> getBadgesByUser(@PathVariable Integer userId) {
        return userBadgeService.getByUserId(userId);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody UserBadgeDTO dto) {
        UserBadge userBadge = new UserBadge();
        userBadge.setUserId(dto.getUserId());
        userBadge.setEarnedAt(dto.getEarnedAt());
        userBadge.setShared(dto.getShared());

        Badges badge = new Badges();
        badge.setId(dto.getBadgeId()); // chỉ cần set ID
        userBadge.setBadge(badge);

        UserBadge saved = userBadgeService.save(userBadge);
        return ResponseEntity.ok(saved);
    }

}
