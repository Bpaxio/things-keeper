package ru.bbpax.keeper.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NonNull;
import ru.bbpax.keeper.model.Image;
import ru.bbpax.keeper.model.Ingredient;
import ru.bbpax.keeper.model.Step;
import ru.bbpax.keeper.model.Tag;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class RecipeDto {
    private String id;
    @NonNull
    @JsonIgnore
    private NoteInfo info;
    private Image image;
    @NonNull
    private List<Step> steps;
    @NonNull
    private List<Ingredient> ingredients;
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

    public RecipeDto setCreated(LocalDateTime created) {
        this.info.setCreated(created);
        return this;
    }

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
