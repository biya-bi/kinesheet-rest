package org.rainbow.kinesheet.service;

import java.util.UUID;

import org.rainbow.kinesheet.model.Objective;

public interface ObjectiveService {

    Objective findById(UUID id);

}
