package ru.bbpax.keeper.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
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
@NoArgsConstructor
public class Recipe extends AbstractNote {

    private Image image;
    private List<Step> steps;
    private List<Ingredient> ingredients;
    private String link;

    public Recipe(String title, LocalDateTime created, String description, List<Tag> tags, Image image, List<Step> steps, List<Ingredient> ingredients, String link) {
        this(title, created, description, tags);
        this.image = image;
        this.steps = steps;
        this.ingredients = ingredients;
        this.link = link;
    }

    public Recipe(String title, LocalDateTime created, String description, List<Tag> tags) {
        super(RECIPE, title, created, description, tags);
    }
}
