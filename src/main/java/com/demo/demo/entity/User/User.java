package com.demo.demo.entity.User;

import com.demo.demo.entity.Auth.ForgotPassword;
import com.demo.demo.enums.Gender;
import com.demo.demo.enums.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "Users")
@Getter
@Setter
// Ẩn password và authorities khi serialize JSON
@JsonIgnoreProperties({"password", "authorities"})
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "avatar_url")
    private String avatarUrl;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "fullname")
    private String fullName;

    @Column(name = "gender")
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    // Chú ý: Đánh dấu NOT NULL ở JPA, DB sẽ enforce constraint
    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToOne(mappedBy = "user",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            orphanRemoval = true)
    private ForgotPassword forgotPassword;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "is_noti", nullable = false)
    private Boolean isNoti;
    /**
     * Trước khi persist lên DB, nếu role null thì gán mặc định USER
     * (đảm bảo không ném NPE khi gọi getRole() hoặc getAuthorities())
     */
    @PrePersist
    private void prePersistDefaults() {
        if (this.role == null) {
            this.role = Role.USER;
        }
        if (this.isActive == null) {
            this.isActive = true;
        }
        if (this.isNoti == null) {
            this.isNoti = true;
        }
        if (createdAt == null) createdAt = LocalDateTime.now();
    }

    /**
     * Override getter để trả về USER thay vì null, phòng khi có object load
     * trước khi ensureRole() chạy hoặc data cũ chưa được migrate.
     */
    public Role getRole() {
        return role == null ? Role.USER : role;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // luôn an toàn vì getRole() không bao giờ null
        return List.of(
                new SimpleGrantedAuthority("ROLE_" + getRole().name())
        );
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    // Các phương thức dưới giữ nguyên
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
