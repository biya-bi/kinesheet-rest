package org.rainbow.kinesheet.controller;

import java.net.URI;
import java.util.UUID;

import org.rainbow.kinesheet.model.Objective;
import org.rainbow.kinesheet.repository.ObjectiveRepository;
import org.rainbow.kinesheet.request.ObjectiveWriteRequest;
import org.rainbow.kinesheet.service.AchieverService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/objectives")
@Validated
class ObjectiveController {

    private final ObjectiveRepository objectiveRepository;
    private final AchieverService achieverService;

    ObjectiveController(ObjectiveRepository objectiveRepository,
            AchieverService achieverService) {
        this.objectiveRepository = objectiveRepository;
        this.achieverService = achieverService;
    }

    @PostMapping
    ResponseEntity<Objective> create(@Valid @RequestBody ObjectiveWriteRequest request, UriComponentsBuilder ucb) {
        Objective newObjective = new Objective();

        newObjective.setAchiever(achieverService.getCurrent());
        newObjective.setTitle(request.getTitle());

        Objective savedObjective = objectiveRepository.save(newObjective);

        URI location = ucb.path("/objectives/{id}")
                .buildAndExpand(savedObjective.getId()).toUri();

        return ResponseEntity.created(location).body(savedObjective);
    }

    @PutMapping("/{id}")
    ResponseEntity<Objective> update(@Valid @RequestBody ObjectiveWriteRequest request, @PathVariable UUID id,
            UriComponentsBuilder ucb) {
        Objective existingObjective = objectiveRepository.findById(id).orElse(null);

        if (existingObjective == null) {
            return ResponseEntity.notFound().build();
        }

        existingObjective.setAchiever(achieverService.getCurrent());
        existingObjective.setTitle(request.getTitle());

        Objective savedObjective = objectiveRepository.save(existingObjective);

        return ResponseEntity.ok(savedObjective);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@PathVariable UUID id) {
        Objective objective = objectiveRepository.findById(id).orElse(null);

        if (objective == null) {
            return ResponseEntity.notFound().build();
        }

        objectiveRepository.delete(objective);

        return ResponseEntity.ok().build();
    }

    @GetMapping
    ResponseEntity<Iterable<Objective>> findAll() {
        return ResponseEntity.ok().body(objectiveRepository.findAll());
    }

    @GetMapping("/{id}")
    ResponseEntity<Objective> findById(@PathVariable UUID id) {
        return objectiveRepository.findById(id).map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

}
