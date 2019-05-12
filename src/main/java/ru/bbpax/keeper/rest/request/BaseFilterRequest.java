package ru.bbpax.keeper.rest.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiParam;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import ru.bbpax.keeper.configurarion.serialization.CustomLocalDateDeserializer;
import ru.bbpax.keeper.configurarion.serialization.CustomLocalDateSerializer;

import java.time.LocalDate;

@Data
public class BaseFilterRequest {
    @ApiParam("String, that can be found in 'title' of the note")
    private String title;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonSerialize(using = CustomLocalDateSerializer.class)
    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    @ApiParam("[yyyy-mm-dd] The First date note was created")
    private LocalDate from;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonSerialize(using = CustomLocalDateSerializer.class)
    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    @ApiParam("[yyyy-mm-dd] The Last date note was created")
    private LocalDate to;

    @ApiParam("'value' of tag, which is contained by note")
    private String tag;
}
