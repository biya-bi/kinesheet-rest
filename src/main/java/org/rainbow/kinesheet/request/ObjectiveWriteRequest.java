package org.rainbow.kinesheet.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ObjectiveWriteRequest {

    public static final String TITLE_REQUIRED_MESSAGE = "The title cannot be blank";

    @NotBlank(message = TITLE_REQUIRED_MESSAGE)
    private String title;

}
