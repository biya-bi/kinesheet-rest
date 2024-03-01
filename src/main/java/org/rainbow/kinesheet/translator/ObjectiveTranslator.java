package org.rainbow.kinesheet.translator;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.rainbow.kinesheet.model.Objective;
import org.rainbow.kinesheet.request.ObjectiveWriteRequest;

public final class ObjectiveTranslator {

    private ObjectiveTranslator() {
    }

    public static Objective from(ObjectiveWriteRequest request) {
        Objective objective = new Objective();

        return from(request, objective);
    }

    public static Objective from(ObjectiveWriteRequest request, Objective objective) {
        objective.setTitle(request.getTitle());
        objective.setAsOf(LocalDateTime.ofEpochSecond(request.getAsOf(), 0, ZoneOffset.UTC));
        objective.setDeadline(LocalDateTime.ofEpochSecond(request.getDeadline(), 0, ZoneOffset.UTC));

        return objective;
    }

    public static ObjectiveWriteRequest to(Objective objective) {
        ObjectiveWriteRequest request = new ObjectiveWriteRequest();

        request.setId(objective.getId());
        request.setTitle(objective.getTitle());
        request.setAsOf(objective.getAsOf().toEpochSecond(ZoneOffset.UTC));
        request.setDeadline(objective.getDeadline().toEpochSecond(ZoneOffset.UTC));

        return request;
    }

}
