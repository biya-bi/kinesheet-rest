package org.rainbow.kinesheet.repository;

import java.util.UUID;

import org.rainbow.kinesheet.model.Achiever;
import org.springframework.data.repository.CrudRepository;

public interface AchieverRepository extends CrudRepository<Achiever, UUID> {

    Achiever findByEmail(String email);

}
