package com.demo.demo.repository.Badges;

import com.demo.demo.entity.Badges.UserBadge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserBadgesRepository extends JpaRepository<UserBadge, Integer> {
    List<UserBadge> findByUserId(Integer userId);
}