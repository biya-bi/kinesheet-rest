package org.rainbow.kinesheet.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class Objective {
    private String id;
    private String title;
    @JsonIgnore
    private User user;
}
