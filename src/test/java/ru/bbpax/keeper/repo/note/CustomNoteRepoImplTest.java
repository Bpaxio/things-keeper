package ru.bbpax.keeper.repo.note;

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

import static org.assertj.core.api.Assertions.assertThat;
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
class CustomNoteRepoImplTest {
    private final List<Tag> tags =
            Arrays.asList(tag("First"), tag("Second"), tag("Third"));
    private final List<Note> notes =
            Arrays.asList(note(), note(), note());

    @Autowired
    private NoteRepo repo;

    @BeforeEach
    void setUp(@Autowired MongoTemplate template) {
        tags.forEach(template::save);
        notes.forEach(note -> note.setTags(tags));
        notes.forEach(template::save);

        final Recipe recipe = recipe();
        recipe.setTags(tags.subList(2, 3));
        final LinkMark linkMark = linkMark();
        linkMark.setTags(tags.subList(1, 3));
        template.save(recipe);
        template.save(linkMark);
        log.info("all: {}", template.findAll(Note.class));
    }

    @Test
    void findAllByTagId() {
        List<Note> allByTagId = repo.findAllByTagId(tags.get(0).getId());

        assertNotNull(allByTagId);
        assertEquals(notes.size(), allByTagId.size());
        assertEquals(notes, allByTagId);

        allByTagId = repo.findAllByTagId(tags.get(1).getId());

        assertNotNull(allByTagId);
        assertEquals(notes.size() + 1, allByTagId.size());
        assertThat(allByTagId).contains(notes.toArray(new Note[0]));

        allByTagId = repo.findAllByTagId(tags.get(2).getId());

        assertNotNull(allByTagId);
        assertEquals(notes.size() + 2, allByTagId.size());
        assertThat(allByTagId).contains(notes.toArray(new Note[0]));

        assertThrows(NullPointerException.class, () -> repo.findAllByTagId("not-existed-ID"));
        assertThrows(IllegalArgumentException.class, () -> repo.findAllByTagId(null));
    }
}