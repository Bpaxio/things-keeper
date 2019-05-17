package ru.bbpax.keeper.security.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Privilege {
    private String noteId;
    private Set<String> accessLevels;
}
