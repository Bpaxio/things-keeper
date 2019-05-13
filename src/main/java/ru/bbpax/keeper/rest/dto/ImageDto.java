package ru.bbpax.keeper.rest.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ImageDto {
    private String id;
    private String link;
    private String originalName;
}
