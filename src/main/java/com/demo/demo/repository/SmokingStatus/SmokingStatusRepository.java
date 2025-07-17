package com.demo.demo.repository.SmokingStatus;

import org.springframework.data.jpa.repository.JpaRepository;

import com.demo.demo.entity.SmokingStatus.SmokingStatus;

import java.util.Optional;

public interface SmokingStatusRepository extends JpaRepository<SmokingStatus, Integer> {
    Optional<SmokingStatus> findByUserId(Integer userId);
}
