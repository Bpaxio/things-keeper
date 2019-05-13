package ru.bbpax.keeper.service.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import ru.bbpax.keeper.configurarion.serialization.CustomLocalDateTimeDeserializer;
import ru.bbpax.keeper.configurarion.serialization.CustomLocalDateTimeSerializer;
import ru.bbpax.keeper.model.Tag;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoteInfo {
    @NonNull
    private String title;
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime created;
    @NonNull
    private String description;
    private List<Tag> tags;
}
