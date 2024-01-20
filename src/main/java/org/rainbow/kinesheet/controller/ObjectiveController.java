package org.rainbow.kinesheet.controller;

import java.net.URI;
import java.util.HashSet;
import java.util.Set;

import org.rainbow.kinesheet.model.Achiever;
import org.rainbow.kinesheet.model.Objective;
import org.rainbow.kinesheet.repository.AchieverRepository;
import org.rainbow.kinesheet.repository.ObjectiveRepository;
import org.rainbow.kinesheet.service.AchieverService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
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
    private final AchieverService achieverService;

    ObjectiveController(ObjectiveRepository objectiveRepository, AchieverService achieverService) {
        this.objectiveRepository = objectiveRepository;
        this.achieverService = achieverService;
    }

    @PostMapping
    ResponseEntity<Objective> create(@RequestBody Objective objective, UriComponentsBuilder ucb,
            JwtAuthenticationToken token) {
        Achiever achiever = achieverService.getCurrent();

        Objective newObjective = new Objective();

        newObjective.setAchiever(achiever);
        newObjective.setTitle(objective.getTitle());

        Objective savedObjective = objectiveRepository.save(newObjective);

        URI location = ucb.path("/objectives/{id}")
                .buildAndExpand(savedObjective.getId()).toUri();

        return ResponseEntity.created(location).body(savedObjective);
    }

    @GetMapping
    ResponseEntity<Iterable<Objective>> findAll() {
        return ResponseEntity.ok().body(objectiveRepository.findAll());
    }
}
