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
public class RecipeDto {
    private String id;
    @NonNull
    @JsonIgnore
    private NoteInfo info;
    private ImageDto image;
    @NonNull
    private List<StepDto> steps;
    @NonNull
    private List<IngredientDto> ingredients;
    private String link;

    public RecipeDto() {
        this.info = new NoteInfo();
    }

    public RecipeDto setId(String id) {
        this.id = id;
        return this;
    }

    public String getId() {
        return id;
    }

    public RecipeDto setTitle(@NonNull String title) {
        this.info.setTitle(title);
        return this;
    }

    public String getTitle() {
        return info.getTitle();
    }

    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    public RecipeDto setCreated(LocalDateTime created) {
        this.info.setCreated(created);
        return this;
    }

    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    public LocalDateTime getCreated() {
        return info.getCreated();
    }

    public RecipeDto setDescription(@NonNull String description) {
        this.info.setDescription(description);
        return this;
    }

    public String getDescription() {
        return info.getDescription();
    }

    public RecipeDto setTags(List<Tag> tags) {
        this.info.setTags(tags);
        return this;
    }

    public List<Tag> getTags() {
        return info.getTags();
    }
}
