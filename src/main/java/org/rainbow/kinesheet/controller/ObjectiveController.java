package org.rainbow.kinesheet.controller;

import java.net.URI;
import java.util.UUID;

import org.rainbow.kinesheet.model.Objective;
import org.rainbow.kinesheet.model.Achiever;
import org.rainbow.kinesheet.repository.ObjectiveRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/objectives")
class ObjectiveController {

    private final ObjectiveRepository objectiveRepository;

    ObjectiveController(ObjectiveRepository objectiveRepository) {
        this.objectiveRepository = objectiveRepository;
    }

    @PostMapping
    ResponseEntity<Objective> create(@RequestBody Objective objective, UriComponentsBuilder ucb) {
        Objective savedObjective = objectiveRepository.save(objective);

        URI location = ucb.path("/objectives/{id}")
                .buildAndExpand(savedObjective.getId()).toUri();

        return ResponseEntity.created(location).body(savedObjective);
    }

    @GetMapping
    ResponseEntity<Iterable<Objective>> findAll() {
        return ResponseEntity.ok().body(objectiveRepository.findAll());
    }
}
