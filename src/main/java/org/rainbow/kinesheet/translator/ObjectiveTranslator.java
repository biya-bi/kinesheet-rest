package org.rainbow.kinesheet.translator;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.rainbow.kinesheet.dto.ObjectiveDto;
import org.rainbow.kinesheet.model.Objective;

public final class ObjectiveTranslator {

    private ObjectiveTranslator() {
    }

    public static Objective translate(ObjectiveDto dto) {
        return translate(dto, new Objective());
    }

    public static Objective translate(ObjectiveDto dto, Objective objective) {
        objective.setTitle(dto.getTitle());
        objective.setAsOf(LocalDateTime.ofEpochSecond(dto.getAsOf(), 0, ZoneOffset.UTC));
        objective.setDeadline(LocalDateTime.ofEpochSecond(dto.getDeadline(), 0, ZoneOffset.UTC));

        return objective;
    }

    public static ObjectiveDto translate(Objective objective) {
        ObjectiveDto dto = new ObjectiveDto();

        dto.setId(objective.getId());
        dto.setTitle(objective.getTitle());
        dto.setAsOf(objective.getAsOf().toEpochSecond(ZoneOffset.UTC));
        dto.setDeadline(objective.getDeadline().toEpochSecond(ZoneOffset.UTC));

        return dto;
    }

}
