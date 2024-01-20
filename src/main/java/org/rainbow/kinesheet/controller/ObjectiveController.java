package org.rainbow.kinesheet.controller;

import java.util.Arrays;
import java.util.UUID;

import org.rainbow.kinesheet.model.Objective;
import org.rainbow.kinesheet.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/objectives")
class ObjectiveController {

    @GetMapping
    ResponseEntity<Iterable<Objective>> findAll() {
        var user1 = new User();
        user1.setId(UUID.randomUUID().toString());
        user1.setEmail("user1@domain.com");
        user1.setName("user1");

        var user2 = new User();
        user2.setId(UUID.randomUUID().toString());
        user2.setEmail("user2@domain.com");
        user2.setName("user2");

        var objective1 = new Objective();
        objective1.setId(UUID.randomUUID().toString());
        objective1.setTitle("Read Java 9 Modularity");
        objective1.setUser(user1);

        var objective2 = new Objective();
        objective2.setId(UUID.randomUUID().toString());
        objective2.setTitle("Learn about CORS");
        objective2.setUser(user1);

        var objective3 = new Objective();
        objective3.setId(UUID.randomUUID().toString());
        objective3.setTitle("Workout thrice weekly");
        objective3.setUser(user2);

        return ResponseEntity.ok().body(Arrays.asList(objective1, objective2, objective3));
    }
}
