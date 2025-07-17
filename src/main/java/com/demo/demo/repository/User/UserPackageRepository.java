package com.demo.demo.repository.User;

import com.demo.demo.entity.User.UserPackage;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface UserPackageRepository extends JpaRepository<UserPackage, Integer> {

    /**
     * Các gói sẽ hết hạn trong ≤ threshold (now + 5 ngày), và chưa nhắc trước
     */
    @Query("""
      SELECT p FROM UserPackage p
      WHERE p.notifiedBeforeCancel = false
        AND p.expiredAt <= :threshold
    """)
    List<UserPackage> findDueBeforeCancel(@Param("threshold") LocalDateTime threshold);

    /**
     * Các gói đã hết hạn (expiredAt ≤ now) và chưa thông báo cancel
     */
    @Query("""
      SELECT p FROM UserPackage p
      WHERE p.notifiedCanceled = false
        AND p.expiredAt <= :now
    """)
    List<UserPackage> findDueCancel(@Param("now") LocalDateTime now);
}
