package com.demo.demo.service;

import com.demo.demo.dto.UserUpdateDTO;
import com.demo.demo.entity.User.User;
import com.demo.demo.repository.User.UserRepository;
import com.demo.demo.enums.Role;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    // Lấy thông tin user
    public User getUserById(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }



    // Cập nhật thông tin user
    public User updateUser(Integer id, UserUpdateDTO dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setFullName(dto.getFullName());
        user.setAvatarUrl(dto.getAvatarUrl());
        return userRepository.save(user);
    }
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }




    // Gán role mới cho user (chỉ giữ 1 role cho user)
    public void changeUserRole(Integer id, String roleName) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        try {
            Role role = Role.valueOf(roleName.toUpperCase());
            user.setRole(role);
            userRepository.save(user);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid role name");
        }
    }


    public void delete(Integer id) {
        userRepository.deleteById(id);
    }

    public Object getUserNameAndMailById(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return new Object() {
            public String getName() {
                return user.getFullName();
            }

            public String getEmail() {
                return user.getEmail();
            }
        };
    }
}
