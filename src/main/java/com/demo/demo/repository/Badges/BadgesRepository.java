package com.demo.demo.repository.Badges;


import com.demo.demo.entity.Badges.Badges;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface BadgesRepository extends JpaRepository<Badges, Integer> {
       Optional<Badges> findById(Integer id);
       // BadgesRepository
       List<Badges> findByBadgetype(String badgetype);

}
