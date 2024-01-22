package org.rainbow.kinesheet.service.impl;

import java.util.UUID;

import org.rainbow.kinesheet.model.Objective;
import org.rainbow.kinesheet.repository.ObjectiveRepository;
import org.rainbow.kinesheet.service.ObjectiveService;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.stereotype.Service;

@Service
class ObjectiveServiceImpl implements ObjectiveService {

    private final ObjectiveRepository objectiveRepository;

    ObjectiveServiceImpl(ObjectiveRepository objectiveRepository) {
        this.objectiveRepository = objectiveRepository;
    }

    @PostAuthorize("returnObject == null || returnObject.achiever.email.equalsIgnoreCase(authentication.tokenAttributes['email'])")
    @Override
    public Objective findById(UUID id) {
        return objectiveRepository.findById(id).orElse(null);
    }

}
