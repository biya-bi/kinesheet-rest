package org.rainbow.kinesheet.authentication;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Claims {
    private final String email;
    private final String name;
}
