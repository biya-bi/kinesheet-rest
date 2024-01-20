package org.rainbow.kinesheet.repository;

import java.util.UUID;

import org.rainbow.kinesheet.model.Objective;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface ObjectiveRepository extends CrudRepository<Objective, UUID> {

    @Query("select o from Objective o where o.achiever.email = :#{authentication.tokenAttributes['email']}")
	Iterable<Objective> findAll();

}
