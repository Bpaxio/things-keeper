package ru.bbpax.keeper.repo.tag;

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
import ru.bbpax.keeper.model.Tag;
import ru.bbpax.keeper.repo.tag.TagRepo;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static ru.bbpax.keeper.util.EntityUtil.tag;

@Slf4j
@DataMongoTest
@ExtendWith(SpringExtension.class)
@TestPropertySource(value = "classpath:application-test.yml")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("test")
class TagRepoTest {
    private final List<Tag> tags =
            Arrays.asList(tag("First"), tag("Second"), tag("Third"));

    @Autowired
    private TagRepo repo;

    @BeforeEach
    void setUp(@Autowired MongoTemplate template) {
        tags.forEach(template::save);
    }

    @Test
    void testCreate(@Autowired MongoTemplate template) {
        int count = template.findAll(Tag.class, "tags").size();
        final Tag tag = repo.save(tag("test"));
        assertEquals(count + 1, template.findAll(Tag.class, "tags").size());
        assertEquals(tag, template.findById(tag.getId(), Tag.class));
    }

    @Test
    void testUpdate(@Autowired MongoTemplate template) {
        int count = template.findAll(Tag.class, "tags").size();
        final Tag tag = template.findById(tags.get(0).getId(), Tag.class);
        tag.setValue("nefeadf");
        repo.save(tag);
        assertEquals(count, template.findAll(Tag.class, "tags").size());
        assertEquals(tag, template.findById(tag.getId(), Tag.class));
    }

    @Test
    @SuppressWarnings("OptionalGetWithoutIsPresent")
    void testFindById() {
        assertNotNull(repo.findById(tags.get(0).getId()).get());
        assertEquals(tags.get(0), repo.findById(tags.get(0).getId()).get());
        assertNotNull(repo.findById(tags.get(1).getId()).get());
        assertEquals(tags.get(1), repo.findById(tags.get(1).getId()).get());
        assertNotNull(repo.findById(tags.get(2).getId()).get());
        assertEquals(tags.get(2), repo.findById(tags.get(2).getId()).get());
    }

    @Test
    void testFindAll(@Autowired MongoTemplate template) {
        final List<Tag> all = repo.findAll();
        assertEquals(template.findAll(Tag.class, "tags").size(), all.size());
        assertEquals(template.findAll(Tag.class, "tags"), all);
    }

    @Test
    void testDelete(@Autowired MongoTemplate template) {
        int count = template.findAll(Tag.class, "tags").size();
        repo.deleteById(tags.get(0).getId());
        assertEquals(count - 1, template.findAll(Tag.class, "tags").size());
    }
}
