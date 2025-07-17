package com.demo.demo.service;

import com.demo.demo.entity.Badges.UserBadge;
import com.demo.demo.repository.Badges.UserBadgesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserBadgeService {

    @Autowired
    private UserBadgesRepository userBadgeRepository;

    public UserBadge save(UserBadge userBadge) {
        return userBadgeRepository.save(userBadge);
    }

    public List<UserBadge> getByUserId(Integer userId) {
        return userBadgeRepository.findByUserId(userId);
    }
}