package ru.bbpax.keeper.repo.linkmark;

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
class LinkMarkRepoTest {
    private final List<Tag> tags =
            Arrays.asList(tag("First"), tag("Second"), tag("Third"));
    private final List<LinkMark> linkMarks =
                Arrays.asList(linkMark(), linkMark(), linkMark());

    @Autowired
    private LinkMarkRepo repo;

    @BeforeEach
    void setUp(@Autowired MongoTemplate template) throws Exception {
        tags.forEach(template::save);
        linkMarks.forEach(linkMark -> linkMark.setTags(tags));
        linkMarks.forEach(template::save);

        final Note note = note();
        note.setTags(tags);
        final Recipe recipe = recipe();
        recipe.setTags(tags);
        template.save(note);
        template.save(recipe);
        log.info("all: {}", template.findAll(Note.class));
    }

    @Test
    void testCreate(@Autowired MongoTemplate template) throws Exception {
        int count = template.findAll(LinkMark.class, "notes").size();
        final LinkMark save = linkMark();
        save.setTags(tags);
        final LinkMark linkMark = repo.save(save);
        assertEquals(count + 1, template.findAll(LinkMark.class, "notes").size());
        assertEquals(linkMark, template.findById(linkMark.getId(), LinkMark.class));
    }

    @Test
    void testUpdate(@Autowired MongoTemplate template) throws Exception {
        int count = template.findAll(LinkMark.class, "notes").size();
        final LinkMark recipe = template.findById(linkMarks.get(0).getId(), LinkMark.class);
        recipe.setDescription("another description");
        repo.save(recipe);
        assertEquals(count, template.findAll(LinkMark.class, "notes").size());
        assertEquals(recipe, template.findById(recipe.getId(), LinkMark.class));
    }

    @Test
    @SuppressWarnings("OptionalGetWithoutIsPresent")
    void testFindById() throws Exception {
        assertNotNull(repo.findById(linkMarks.get(0).getId()).get());
        assertEquals(linkMarks.get(0), repo.findById(linkMarks.get(0).getId()).get());
        assertNotNull(repo.findById(linkMarks.get(1).getId()).get());
        assertEquals(linkMarks.get(1), repo.findById(linkMarks.get(1).getId()).get());
        assertNotNull(repo.findById(linkMarks.get(2).getId()).get());
        assertEquals(linkMarks.get(2), repo.findById(linkMarks.get(2).getId()).get());
    }

    @Test
    void testFindAll(@Autowired MongoTemplate template) throws Exception {
        final List<LinkMark> all = repo.findAll();
        assertEquals(linkMarks.size(), all.size());
        assertEquals(linkMarks, all);
    }

    @Test
    void testDelete(@Autowired MongoTemplate template) throws Exception {
        int count = template.findAll(LinkMark.class, "notes").size();
        repo.deleteById(linkMarks.get(0).getId());
        assertEquals(count - 1, template.findAll(LinkMark.class, "notes").size());
    }
}
