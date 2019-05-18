package ru.bbpax.keeper.model;

import com.querydsl.core.annotations.QueryEmbedded;
import com.querydsl.core.annotations.QueryEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

import static ru.bbpax.keeper.model.NoteTypes.RECIPE;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Document(collection = "notes")
@TypeAlias(RECIPE)
@QueryEntity
public class Recipe extends AbstractNote {

    private String category;
    @QueryEmbedded
    private Image image;
    @QueryEmbedded
    private List<Step> steps;
    @QueryEmbedded
    private List<Ingredient> ingredients;
    private String link;

    public Recipe(String id, String title, LocalDateTime created, String createdBy, String description, List<Tag> tags, String category, Image image, List<Step> steps, List<Ingredient> ingredients, String link) {
        super(id, title, created, createdBy, description, tags, RECIPE);
        this.image = image;
        this.steps = steps;
        this.ingredients = ingredients;
        this.link = link;
    }

    public Recipe(String title, LocalDateTime created, String description, List<Tag> tags, String category, Image image, List<Step> steps, List<Ingredient> ingredients, String link) {
        super(title, created, description, tags, RECIPE);
        this.image = image;
        this.steps = steps;
        this.ingredients = ingredients;
        this.link = link;
    }

    public Recipe() {
        super(RECIPE);
    }
}
