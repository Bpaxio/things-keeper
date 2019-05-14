package ru.bbpax.keeper.rest.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.bbpax.keeper.configurarion.serialization.CustomBigDecimalDeserializer;
import ru.bbpax.keeper.configurarion.serialization.CustomBigDecimalSerializer;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class IngredientDto {
    private String id;
    private String name;
    @JsonSerialize(using = CustomBigDecimalSerializer.class)
    @JsonDeserialize(using = CustomBigDecimalDeserializer.class)
    private BigDecimal value;
    private String unit;
}
