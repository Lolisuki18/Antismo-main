package com.demo.demo.entity.User;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "UserPackages")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class UserPackage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_id",        nullable = false)
    private Integer userId;

    @Column(name = "package_id",     nullable = false)
    private Integer packageId;

    /** Thời điểm user mua gói */
    @Column(name = "purchased_at",    nullable = false, updatable = false)
    private LocalDateTime purchasedAt;

    /** Thời điểm gói hết hạn (30 ngày sau mua) */
    @Column(name = "expired_at",      nullable = false)
    private LocalDateTime expiredAt;

    /** Đã gửi nhắc trước 5 ngày chưa */
    @Column(name = "notified_before_cancel", nullable = false)
    private Boolean notifiedBeforeCancel = false;

    /** Đã gửi notification khi cancel chưa */
    @Column(name = "notified_canceled",      nullable = false)
    private Boolean notifiedCanceled       = false;

    @PrePersist
    protected void onCreate() {
        if (purchasedAt == null) {
            this.purchasedAt = LocalDateTime.now();
        }
    }
}
