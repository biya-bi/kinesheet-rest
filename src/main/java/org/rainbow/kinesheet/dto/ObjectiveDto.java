package org.rainbow.kinesheet.dto;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ObjectiveDto {

    public static final String TITLE_REQUIRED_MESSAGE = "The title cannot be blank";

    private UUID id;

    @NotBlank(message = TITLE_REQUIRED_MESSAGE)
    private String title;

    private long asOf;

    private long deadline;
}
