package com.demo.demo.repository.User;

import com.demo.demo.entity.User.User;
import com.demo.demo.enums.Role;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findUserByEmail(String email);
    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.password = ?2 WHERE u.email = ?1")
    void updateUserPasswordByEmail(String email, String password);
    @Query("SELECT u FROM User u WHERE u.isActive = true AND u.isNoti = true")
    List<User> findAllActiveNotiUsers();
    List<User> findAllByIsActiveTrue();
    List<User> findByRoleAndIsActiveTrue(Role role);


}
