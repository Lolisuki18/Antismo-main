package com.demo.demo.api;

import com.demo.demo.entity.Badges.Badges;
import com.demo.demo.repository.Badges.BadgesRepository;
import com.demo.demo.service.BadgesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/badges")
@CrossOrigin(origins = "*")
public class BadgesAPI {

    @Autowired
    private BadgesService badgesService;

    @Autowired
    private BadgesRepository badgesRepository;

//    @GetMapping
//    public ResponseEntity<Badges> create(@RequestBody Badges badges){
//        Badges savedBadges = badgesService.save(badges);
//        return ResponseEntity.ok(savedBadges);
//    }

//    @GetMapping("/{id}")
//    public ResponseEntity<Badges> getById(@PathVariable("id") Integer id){
//        Badges badges = badgesService.findById(id);
//        return ResponseEntity.ok(badges);
//    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Integer id){
        badgesService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<Badges> createBadges(@RequestBody Badges badges){
        Badges savedBadges = badgesService.save(badges);
        return ResponseEntity.ok(savedBadges);
    }

    // BadgesAPI
    @GetMapping
    public ResponseEntity<Iterable<Badges>> getAllBadges() {
        Iterable<Badges> badges = badgesService.findAll();
        return ResponseEntity.ok(badges);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBadgesById(@PathVariable("id") Integer id) {
        Optional<Badges> badges = badgesRepository.findById(id);
        return ResponseEntity.ok(badges);
    }

    @GetMapping("/")
    public ResponseEntity<?> getByType(@RequestParam(required = true) String type) {
        if (type != null) {
            return ResponseEntity.ok(badgesService.getByType(type));
        }
        return ResponseEntity.ok(badgesService);
    }

}
