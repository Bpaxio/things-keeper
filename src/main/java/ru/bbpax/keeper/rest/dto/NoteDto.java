package ru.bbpax.keeper.rest.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
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
public class NoteDto {
    private String id;
    @NonNull
    @JsonIgnore
    private NoteInfo info;

    public NoteDto() {
        this.info = new NoteInfo();
    }

    public NoteDto(@NonNull String title, LocalDateTime created, @NonNull String description, List<Tag> tags) {
        this.info = new NoteInfo(title, created, description, tags);
    }

    public NoteDto setId(String id) {
        this.id = id;
        return this;
    }

    public String getId() {
        return id;
    }

    public NoteDto setTitle(@NonNull String title) {
        this.info.setTitle(title);
        return this;
    }

    public String getTitle() {
        return info.getTitle();
    }

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME, pattern = DATE_TIME_PATTERN)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    public NoteDto setCreated(LocalDateTime created) {
        this.info.setCreated(created);
        return this;
    }

    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    public LocalDateTime getCreated() {
        return info.getCreated();
    }

    public NoteDto setDescription(@NonNull String description) {
        this.info.setDescription(description);
        return this;
    }

    public String getDescription() {
        return info.getDescription();
    }

    public NoteDto setTags(List<Tag> tags) {
        this.info.setTags(tags);
        return this;
    }

    public List<Tag> getTags() {
        return info.getTags();
    }

}
