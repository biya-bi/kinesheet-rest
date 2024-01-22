package org.rainbow.kinesheet.repository;

import java.util.Optional;
import java.util.UUID;

import org.rainbow.kinesheet.model.Objective;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.security.access.prepost.PostAuthorize;

public interface ObjectiveRepository extends CrudRepository<Objective, UUID> {

    @PostAuthorize("returnObject.isEmpty() || returnObject.get().achiever.email.equalsIgnoreCase(authentication.tokenAttributes['email'])")
    Optional<Objective> findById(UUID id);

    @Query("select o from Objective o where o.achiever.email = :#{authentication.tokenAttributes['email']}")
    Iterable<Objective> findAll();

}
