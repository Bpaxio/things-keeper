package ru.bbpax.keeper.util;

import org.bson.types.ObjectId;
import org.modelmapper.ModelMapper;
import ru.bbpax.keeper.model.Image;
import ru.bbpax.keeper.model.Ingredient;
import ru.bbpax.keeper.model.LinkMark;
import ru.bbpax.keeper.model.Note;
import ru.bbpax.keeper.model.Recipe;
import ru.bbpax.keeper.model.Step;
import ru.bbpax.keeper.model.Tag;
import ru.bbpax.keeper.rest.dto.IngredientDto;
import ru.bbpax.keeper.rest.dto.LinkMarkDto;
import ru.bbpax.keeper.rest.dto.NoteDto;
import ru.bbpax.keeper.rest.dto.RecipeDto;
import ru.bbpax.keeper.rest.dto.StepDto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Random;

public class EntityUtil {
    private static final ModelMapper mapper = new ModelMapper();

    public static Tag tag() {
        return tag("See Tomorrow");
    }

    public static Tag tag(String tagValue) {
        return new Tag(new ObjectId().toHexString(), tagValue);
    }

    public static NoteDto noteDto() {
        return mapper.map(note(), NoteDto.class);
    }

    public static Note note() {
        return new Note(
                new ObjectId().toHexString(),
                "Title",
                LocalDateTime.now(),
                "Description",
                Arrays.asList(tag("First"), tag("Second"), tag("Third"))
        );
    }

    public static LinkMarkDto linkMarkDto() {
        return mapper.map(linkMark(), LinkMarkDto.class);
    }

    public static LinkMark linkMark() {
        return new LinkMark(
                new ObjectId().toHexString(),
                "Title",
                LocalDateTime.now(),
                "Description",
                Arrays.asList(tag("First"), tag("Second"), tag("Third")),
                "link"
        );
    }

    public static RecipeDto recipeDto() {
        return mapper.map(recipe(), RecipeDto.class);
    }

    public static Recipe recipe() {
        return new Recipe(
                new ObjectId().toHexString(),
                "Title",
                LocalDateTime.now(),
                "Description",
                Arrays.asList(tag("First"), tag("Second"), tag("Third")),
                new Image("select", "image.jpg"),
                Arrays.asList(step(1), step(2), step(3)),
                Arrays.asList(ingredient(), ingredient(), ingredient()),
                "link"

        );
    }

    public static StepDto stepDto(int number) {
        return mapper.map(step(number), StepDto.class);
    }

    public static Step step(int number) {
        return new Step(
                "step " + number,
                number,
                "step descr",
                new Image("query", "image" + number + ".jpg")
        );
    }

    public static IngredientDto ingredientDto() {
        return mapper.map(ingredient(), IngredientDto.class);
    }

    public static Ingredient ingredient() {
        final Random random = new Random();
        return new Ingredient(
                "tasty food " + random.nextInt(300),
                BigDecimal.valueOf(300 * random.nextDouble()).setScale(2, RoundingMode.HALF_UP),
                "kg"
        );
    }
}
