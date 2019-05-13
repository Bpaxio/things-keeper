package ru.bbpax.keeper.model;

import com.querydsl.core.annotations.QueryEmbeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@QueryEmbeddable
public class Ingredient {
    @Id
    @Field("_id")
    private String id;
    private String name;
    private BigDecimal value;
    private String unit;

    public Ingredient(String name, BigDecimal value, String unit) {
        this();
        this.name = name;
        this.value = value;
        this.unit = unit;
    }
}
