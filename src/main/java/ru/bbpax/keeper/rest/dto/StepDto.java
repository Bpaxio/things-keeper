package ru.bbpax.keeper.rest.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.bson.types.ObjectId;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StepDto {

    private String id;
    private String title;
    private int stepNumber;
    private String description;

    private ImageDto image;

    public StepDto() {
        this.id = new ObjectId().toHexString();
    }
}
