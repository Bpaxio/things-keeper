package ru.bbpax.keeper.repo;

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

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
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
class CustomTagRepoImplTest {
    private final List<Tag> tags =
            Arrays.asList(tag("First"), tag("Second"), tag("Third"));
    private final Tag tag = tag("Unused");

    @Autowired
    private TagRepo repo;

    @BeforeEach
    void setUp(@Autowired MongoTemplate template) {
        tags.forEach(template::save);
        template.save(tag);

        final Note note = note();
        note.setTags(tags);
        final Recipe recipe = recipe();
        recipe.setTags(tags);
        final LinkMark linkMark = linkMark();
        linkMark.setTags(tags);
        template.save(note);
        template.save(recipe);
        template.save(linkMark);
        log.info("tags: {}\nall: {}",
                template.findAll(Tag.class),
                template.findAll(Note.class));
    }

    @Test
    void tagIsUsed() {
        tags.forEach(tag -> assertTrue(repo.tagIsUsed(tag)));
        assertFalse(repo.tagIsUsed(tag));
        assertThrows(IllegalArgumentException.class, () -> repo.tagIsUsed(null));
    }
}