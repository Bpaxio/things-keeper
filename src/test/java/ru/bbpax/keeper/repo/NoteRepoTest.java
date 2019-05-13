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
import ru.bbpax.keeper.repo.note.NoteRepo;

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
class NoteRepoTest {
    private final List<Tag> tags =
            Arrays.asList(tag("First"), tag("Second"), tag("Third"));
    private final List<Note> notes =
                Arrays.asList(note(), note(), note());

    @Autowired
    private NoteRepo repo;

    @BeforeEach
    void setUp(@Autowired MongoTemplate template) throws Exception {
        tags.forEach(template::save);
        notes.forEach(note -> note.setTags(tags));
        notes.forEach(template::save);

        final Recipe recipe = recipe();
        recipe.setTags(tags);
        final LinkMark linkMark = linkMark();
        linkMark.setTags(tags);
        template.save(recipe);
        template.save(linkMark);
        log.info("all: {}", template.findAll(Note.class));
    }

    @Test
    void testCreate(@Autowired MongoTemplate template) throws Exception {
        int count = template.findAll(Note.class, "notes").size();
        final Note save = note();
        save.setTags(tags);
        final Note note = repo.save(save);
        assertEquals(count + 1, template.findAll(Note.class, "notes").size());
        assertEquals(note, template.findById(note.getId(), Note.class));
    }

    @Test
    void testUpdate(@Autowired MongoTemplate template) throws Exception {
        int count = template.findAll(Note.class, "notes").size();
        final Note note = template.findById(notes.get(0).getId(), Note.class);
        note.setDescription("another description");
        repo.save(note);
        assertEquals(count, template.findAll(Note.class, "notes").size());
        assertEquals(note, template.findById(note.getId(), Note.class));
    }

    @Test
    @SuppressWarnings("OptionalGetWithoutIsPresent")
    void testFindById() throws Exception {
        assertNotNull(repo.findById(notes.get(0).getId()).get());
        assertEquals(notes.get(0), repo.findById(notes.get(0).getId()).get());
        assertNotNull(repo.findById(notes.get(1).getId()).get());
        assertEquals(notes.get(1), repo.findById(notes.get(1).getId()).get());
        assertNotNull(repo.findById(notes.get(2).getId()).get());
        assertEquals(notes.get(2), repo.findById(notes.get(2).getId()).get());
    }

    @Test
    void testFindAll(@Autowired MongoTemplate template) throws Exception {
        final List<Note> all = repo.findAll();
        assertEquals(notes.size(), all.size());
        assertEquals(notes, all);
    }

    @Test
    void testDelete(@Autowired MongoTemplate template) throws Exception {
        int count = template.findAll(Note.class, "notes").size();
        repo.deleteById(notes.get(0).getId());
        assertEquals(count - 1, template.findAll(Note.class, "notes").size());
    }
}
