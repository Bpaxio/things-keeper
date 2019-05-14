package ru.bbpax.keeper.repo.recipe;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.bbpax.keeper.model.LinkMark;
import ru.bbpax.keeper.model.Note;
import ru.bbpax.keeper.model.Recipe;
import ru.bbpax.keeper.model.Tag;
import ru.bbpax.keeper.repo.recipe.RecipeRepo;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static ru.bbpax.keeper.util.EntityUtil.linkMark;
import static ru.bbpax.keeper.util.EntityUtil.note;
import static ru.bbpax.keeper.util.EntityUtil.recipe;
import static ru.bbpax.keeper.util.EntityUtil.tag;

@Slf4j
@DataMongoTest
@ExtendWith(SpringExtension.class)
@TestPropertySource(value = "classpath:application-test.yml")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("test")
class RecipeRepoTest {
    private final List<Tag> tags =
            Arrays.asList(tag("First"), tag("Second"), tag("Third"));
    private final List<Recipe> recipes =
                Arrays.asList(recipe(), recipe(), recipe());

    @Autowired
    private RecipeRepo repo;

    @BeforeEach
    void setUp(@Autowired MongoTemplate template) throws Exception {
        tags.forEach(template::save);
        recipes.forEach(recipe -> recipe.setTags(tags));
        recipes.forEach(template::save);

        final Note note = note();
        note.setTags(tags);
        final LinkMark linkMark = linkMark();
        linkMark.setTags(tags);
        template.save(note);
        template.save(linkMark);
        log.info("all: {}", template.findAll(Note.class));
    }

    @Test
    void testCreate(@Autowired MongoTemplate template) throws Exception {
        int count = template.findAll(Recipe.class, "notes").size();
        final Recipe save = recipe();
        save.setTags(tags);
        final Recipe recipe = repo.save(save);
        assertEquals(count + 1, template.findAll(Recipe.class, "notes").size());
        assertEquals(recipe, template.findById(recipe.getId(), Recipe.class));
    }

    @Test
    void testUpdate(@Autowired MongoTemplate template) throws Exception {
        int count = template.findAll(Recipe.class, "notes").size();
        final Recipe recipe = template.findById(recipes.get(0).getId(), Recipe.class);
        recipe.setDescription("another description");
        repo.save(recipe);
        assertEquals(count, template.findAll(Recipe.class, "notes").size());
        assertEquals(recipe, template.findById(recipe.getId(), Recipe.class));
    }

    @Test
    @SuppressWarnings("OptionalGetWithoutIsPresent")
    void testFindById() throws Exception {
        assertNotNull(repo.findById(recipes.get(0).getId()).get());
        assertEquals(recipes.get(0), repo.findById(recipes.get(0).getId()).get());
        assertNotNull(repo.findById(recipes.get(1).getId()).get());
        assertEquals(recipes.get(1), repo.findById(recipes.get(1).getId()).get());
        assertNotNull(repo.findById(recipes.get(2).getId()).get());
        assertEquals(recipes.get(2), repo.findById(recipes.get(2).getId()).get());
    }

    @Test
    void testFindAll(@Autowired MongoTemplate template) throws Exception {
        final List<Recipe> all = repo.findAll();
        assertEquals(recipes.size(), all.size());
        assertEquals(recipes, all);
    }

    @Test
    void testDelete(@Autowired MongoTemplate template) throws Exception {
        int count = template.findAll(Recipe.class, "notes").size();
        repo.deleteById(recipes.get(0).getId());
        assertEquals(count - 1, template.findAll(Recipe.class, "notes").size());
    }
}
