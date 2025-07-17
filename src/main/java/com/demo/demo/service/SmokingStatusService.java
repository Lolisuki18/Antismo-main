package com.demo.demo.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.demo.entity.User.User;
import com.demo.demo.entity.SmokingStatus.SmokingStatus;
import com.demo.demo.repository.User.UserRepository;
import com.demo.demo.repository.SmokingStatus.SmokingStatusRepository;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class SmokingStatusService {

    @Autowired
    private SmokingStatusRepository smokingStatusRepository;

    @Autowired
    private UserRepository userRepository;

    public SmokingStatus create(SmokingStatus status) {
        status.setRecordedAt(LocalDateTime.now());
        return smokingStatusRepository.save(status);
    }
//    public List<SmokingStatus> getByUserId(Long userId) {
//        return smokingStatusRepository.findByUserId(userId);
//    }

    public Optional<SmokingStatus> getByUserEmail(String email) {
        User user = userRepository.findUserByEmail(email)
                .orElse(null);
        if (user == null) {
            return Optional.empty();
        }
        return smokingStatusRepository.findByUserId(user.getId());
    }

    public Optional<SmokingStatus> getByUserId(Integer id) {
        return smokingStatusRepository.findByUserId(id);
    }

//    public SmokingStatus update(Long id, SmokingStatus updated) {
//        return smokingStatusRepository.findById(id).map(existing -> {
//            existing.setCigarettesPerDay(updated.getCigarettesPerDay());
//            existing.setFrequency(updated.getFrequency());
//            existing.setCostPerPack(updated.getCostPerPack());
//            existing.setRecordedAt(updated.getRecordedAt());
//            existing.setNote(updated.getNote());
//            existing.setEmotion(updated.getEmotion());
//            existing.setLocation(updated.getLocation());
//            existing.setIsQuitting(updated.getIsQuitting());
//            return smokingStatusRepository.save(existing);
//        }).orElseThrow(() -> new RuntimeException("Không tìm thấy trạng thái"));
//    }

    public void delete(Integer id) {
        smokingStatusRepository.deleteById(id);
    }
}
