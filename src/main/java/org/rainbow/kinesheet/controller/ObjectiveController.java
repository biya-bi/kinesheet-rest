package org.rainbow.kinesheet.controller;

import java.net.URI;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.rainbow.kinesheet.dto.ObjectiveDto;
import org.rainbow.kinesheet.model.Objective;
import org.rainbow.kinesheet.repository.ObjectiveRepository;
import org.rainbow.kinesheet.service.AchieverService;
import org.rainbow.kinesheet.translator.ObjectiveTranslator;
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

/**
 * Represents the controller used for servicing REST requests about objectives.
 * 
 * The methods of this class that require accessing existing {@link Objective}s
 * are secure since security has been achieved at the level of the
 * {@link ObjectiveRepository}.
 */
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
    ResponseEntity<Objective> create(@Valid @RequestBody ObjectiveDto dto, UriComponentsBuilder ucb) {
        Objective newObjective = ObjectiveTranslator.translate(dto);

        // IDs are auto-generated in the data access layer. So we need to make sure that
        // the ID on the new objective is null.
        newObjective.setId(null);
        newObjective.setAchiever(achieverService.getCurrent());

        Objective savedObjective = objectiveRepository.save(newObjective);

        URI location = ucb.path("/objectives/{id}")
                .buildAndExpand(savedObjective.getId()).toUri();

        return ResponseEntity.created(location).body(savedObjective);
    }

    @PutMapping("/{id}")
    ResponseEntity<ObjectiveDto> update(@Valid @RequestBody ObjectiveDto dto,
            @PathVariable UUID id) {
        Objective existingObjective = objectiveRepository.findById(id).orElse(null);

        if (existingObjective == null) {
            return ResponseEntity.notFound().build();
        }

        ObjectiveTranslator.translate(dto, existingObjective);

        Objective savedObjective = objectiveRepository.save(existingObjective);

        return ResponseEntity.ok(ObjectiveTranslator.translate(savedObjective));
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
    ResponseEntity<Iterable<ObjectiveDto>> findAll() {
        List<ObjectiveDto> dtos = StreamSupport.stream(objectiveRepository.findAll().spliterator(), false)
                .map(ObjectiveTranslator::translate).collect(Collectors.toList());
        return ResponseEntity.ok().body(dtos);
    }

    @GetMapping("/{id}")
    ResponseEntity<ObjectiveDto> findById(@PathVariable UUID id) {
        return objectiveRepository.findById(id)
                .map(objective -> ResponseEntity.ok(ObjectiveTranslator.translate(objective)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

}
