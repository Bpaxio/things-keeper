package ru.bbpax.keeper.model;

import com.querydsl.core.annotations.QueryEmbeddable;
import com.querydsl.core.annotations.QueryEmbedded;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@AllArgsConstructor
@QueryEmbeddable
public class Step {
    @Id
    @Field("_id")
    private String id;
    private String title;
    private int stepNumber;
    private String description;
    @QueryEmbedded
    private Image image;

    public Step(String title, int stepNumber, String description, Image image) {
        this();
        this.title = title;
        this.stepNumber = stepNumber;
        this.description = description;
        this.image = image;
    }

    public Step() {
        this.id = new ObjectId().toHexString();
    }
}
