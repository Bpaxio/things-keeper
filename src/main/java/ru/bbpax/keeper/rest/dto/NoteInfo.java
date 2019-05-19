package ru.bbpax.keeper.rest.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import org.springframework.format.annotation.DateTimeFormat;
import ru.bbpax.keeper.configurarion.serialization.CustomLocalDateTimeDeserializer;
import ru.bbpax.keeper.configurarion.serialization.CustomLocalDateTimeSerializer;
import ru.bbpax.keeper.model.Tag;

import java.time.LocalDateTime;
import java.util.List;

import static ru.bbpax.keeper.configurarion.serialization.CustomLocalDateTimeDeserializer.DATE_TIME_PATTERN;

@Data
@AllArgsConstructor
public class NoteInfo {
    @NonNull
    private String title;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME, pattern = DATE_TIME_PATTERN)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime created;
    @NonNull
    private String description;
    private List<Tag> tags;

    public NoteInfo() {
        created = LocalDateTime.now();
    }
}
