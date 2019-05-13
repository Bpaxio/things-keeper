package ru.bbpax.keeper.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.NonNull;
import ru.bbpax.keeper.configurarion.serialization.CustomLocalDateTimeDeserializer;
import ru.bbpax.keeper.configurarion.serialization.CustomLocalDateTimeSerializer;
import ru.bbpax.keeper.model.Tag;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class LinkMarkDto {
    private String id;
    @NonNull
    @JsonIgnore
    private NoteInfo info;
    @NonNull
    private String link;

    public LinkMarkDto() {
        this.info = new NoteInfo();
    }

    public LinkMarkDto setId(String id) {
        this.id = id;
        return this;
    }

    public String getId() {
        return id;
    }

    public LinkMarkDto setTitle(@NonNull String title) {
        this.info.setTitle(title);
        return this;
    }

    public String getTitle() {
        return info.getTitle();
    }

    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    public LinkMarkDto setCreated(LocalDateTime created) {
        this.info.setCreated(created);
        return this;
    }

    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    public LocalDateTime getCreated() {
        return info.getCreated();
    }

    public LinkMarkDto setDescription(@NonNull String description) {
        this.info.setDescription(description);
        return this;
    }

    public String getDescription() {
        return info.getDescription();
    }

    public LinkMarkDto setTags(List<Tag> tags) {
        this.info.setTags(tags);
        return this;
    }

    public List<Tag> getTags() {
        return info.getTags();
    }
}
