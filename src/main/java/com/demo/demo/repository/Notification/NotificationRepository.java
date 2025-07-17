package com.demo.demo.repository.Notification;

import com.demo.demo.entity.Notification.Notification;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer> {

    List<Notification> findByUserIdOrderBySentAtDesc(Integer userId);

    List<Notification> findByTypeAndSentAtBetween(
            String type,
            OffsetDateTime start,
            OffsetDateTime end
    );
}
