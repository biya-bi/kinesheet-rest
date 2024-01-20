package org.rainbow.kinesheet.repository;

import java.util.UUID;

import org.rainbow.kinesheet.model.Objective;
import org.springframework.data.repository.CrudRepository;

public interface ObjectiveRepository extends CrudRepository<Objective, UUID> {
    
}
