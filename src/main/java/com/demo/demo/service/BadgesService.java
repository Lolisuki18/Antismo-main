package com.demo.demo.service;

import com.demo.demo.entity.Badges.Badges;
import com.demo.demo.repository.Badges.BadgesRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BadgesService {
    @Autowired
    private BadgesRepository badgesRepository;  // Changed from BadgesService to BadgesRepository

    public Badges save(Badges badges) {
        return badgesRepository.save(badges);
    }

//    public Badges findById(Integer id) {
//        return badgesRepository.findById(id);
//    }

    public void deleteById(Integer id) {
        badgesRepository.deleteById(id);
    }

   public List<Badges> findAll() {
        return badgesRepository.findAll();
    }
    // BadgesService
    public List<Badges> getByType(String type) {
        return badgesRepository.findByBadgetype(type);
    }


}