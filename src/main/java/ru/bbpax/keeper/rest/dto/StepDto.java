package ru.bbpax.keeper.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StepDto {

    private String id;
    private String title;
    private int stepNumber;
    private String description;

    private ImageDto image;
}
