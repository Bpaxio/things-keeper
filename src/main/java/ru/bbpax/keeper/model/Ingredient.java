package ru.bbpax.keeper.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@AllArgsConstructor
public class Ingredient {
    @Id
    @Field("_id")
    private String id;
    private String name;
    private float value;
    private String unit;

    public Ingredient(String name, float value, String unit) {
        this();
        this.name = name;
        this.value = value;
        this.unit = unit;
    }

    public Ingredient() {
        this.id = new ObjectId().toHexString();
    }
}
