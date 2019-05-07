package ru.bbpax.keeper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import ru.bbpax.keeper.model.Image;
import ru.bbpax.keeper.model.Ingredient;
import ru.bbpax.keeper.model.Note;
import ru.bbpax.keeper.model.Recipe;
import ru.bbpax.keeper.model.Step;
import ru.bbpax.keeper.model.Tag;
import ru.bbpax.keeper.repo.NoteRepo;
import ru.bbpax.keeper.repo.TagRepo;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static ru.bbpax.keeper.model.NoteTypes.LINK_MARK;
import static ru.bbpax.keeper.model.NoteTypes.NOTE;
import static ru.bbpax.keeper.model.NoteTypes.RECIPE;

@Slf4j
@SpringBootApplication
public class ThingsKeeperApp {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(ThingsKeeperApp.class);
        testCreate(context);
        testDelete(context);
    }

    private static void testDelete(ConfigurableApplicationContext context) {
        NoteRepo noteRepo = context.getBean(NoteRepo.class);
        TagRepo tagRepo = context.getBean(TagRepo.class);
    }

    private static void testCreate(ConfigurableApplicationContext context) {
        NoteRepo noteRepo = context.getBean(NoteRepo.class);
        TagRepo tagRepo = context.getBean(TagRepo.class);
        Tag best = tagRepo.save(new Tag("best"));
        Note note = new Note(
                "title",
                LocalDateTime.now(),
                "description",
                Collections.singletonList(best)
        );
        noteRepo.save(note);

        Recipe recipe = new Recipe(
                "title",
                LocalDateTime.now(),
                "description",
                Collections.singletonList(best),
                new Image("selector", "тачечека"),
                Collections.singletonList(
                        new Step(
                                "just do",
                                0,
                                "description",
                                new Image("selector0", "шаг 1")
                        )
                ),
                Arrays.asList(
                        new Ingredient("onion", 300, "gr"),
                        new Ingredient("meal", 500, "gr")
                ),
                "https://link.com/?xz=rere"
        );
        noteRepo.save(recipe);

        final List<Note> notes = noteRepo.findAllByNoteType(NOTE);
        final List<Recipe> recipes = noteRepo.findAllByNoteType(RECIPE);
        log.info("all[{}], notes[{}] : {}", noteRepo.count(), notes.size(), notes);
        log.info("all[{}], recipes[{}] : {}", noteRepo.count(), recipes.size(), recipes);

        List<Recipe> recipes1 = noteRepo.findAllByNoteType(LINK_MARK);
        log.info("all[{}], linkmarks[{}] : {}", noteRepo.count(), recipes1.size(), recipes1);
        recipes1 = noteRepo.findAllByNoteType(NOTE);
        log.info("all[{}], recipes1[{}] : {}", noteRepo.count(), recipes1.size(), recipes1);
    }
}
