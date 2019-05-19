package ru.bbpax.keeper.service.filter;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.bbpax.keeper.model.LinkMark;
import ru.bbpax.keeper.model.Note;
import ru.bbpax.keeper.model.Recipe;
import ru.bbpax.keeper.model.Tag;
import ru.bbpax.keeper.repo.linkmark.LinkMarkRepo;
import ru.bbpax.keeper.rest.request.LinkMarkFilterRequest;
import ru.bbpax.keeper.service.FilterService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.bbpax.keeper.util.EntityUtil.linkMark;
import static ru.bbpax.keeper.util.EntityUtil.note;
import static ru.bbpax.keeper.util.EntityUtil.recipe;
import static ru.bbpax.keeper.util.EntityUtil.tag;

@Slf4j
@DataMongoTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@TestPropertySource(value = "classpath:application-test.yml")
@ActiveProfiles("test")
@Import({ FilterService.class })
class LinkMarkFilterTest {
    private final static List<Tag> tags =
            Arrays.asList(tag("First"), tag("Second"), tag("Third"));
    private final static List<Note> notes =
                Arrays.asList(note(), note(), note());
    private final static List<Recipe> recipes =
            Arrays.asList(recipe(), recipe(), recipe());
    private final static List<LinkMark> linkMarks =
            Arrays.asList(linkMark(), linkMark(), linkMark());

    @Autowired
    private FilterService service;

    @Autowired
    private LinkMarkRepo repo;

    @BeforeAll
    static void setUp(@Autowired MongoTemplate template) {
        tags.forEach(template::save);
        notes.forEach(note -> note.setTags(tags));
        notes.forEach(template::save);

        recipes.forEach(recipe -> recipe.setTags(tags));
        recipes.forEach(template::save);

        linkMarks.forEach(linkMark -> linkMark.setTags(tags));
        linkMarks.forEach(template::save);

        log.info("all: {}", template.findAll(LinkMark.class));
    }

    @ParameterizedTest
    @MethodSource("prepareLinkMarkRequest")
    void testFindAllByLinkMarkFilter(List<LinkMark> expected, LinkMarkFilterRequest request) {
        final List<LinkMark> all = repo.findAll(service.makePredicate(request));
        assertEquals(expected.size(), all.size());
        assertEquals(expected, all);
    }

    private static Stream<Arguments> prepareLinkMarkRequest() {
        List<Arguments> argumentsList = new ArrayList<>();
        LinkMarkFilterRequest request = new LinkMarkFilterRequest();
        argumentsList.add(Arguments.of(linkMarks, request));
        request = new LinkMarkFilterRequest();
        request.setInput("title");
        argumentsList.add(Arguments.of(linkMarks, request));

        request = new LinkMarkFilterRequest();
        request.setInput("Descri");
        argumentsList.add(Arguments.of(Collections.emptyList(), request));

        request = new LinkMarkFilterRequest();
        request.setInput("link");
        argumentsList.add(Arguments.of(Collections.emptyList(), request));

        request = new LinkMarkFilterRequest();
        request.setInput("link");
        request.setDescription(true);
        argumentsList.add(Arguments.of(Collections.emptyList(), request));

        request = new LinkMarkFilterRequest();
        request.setInput("link");
        request.setLink(true);
        argumentsList.add(Arguments.of(linkMarks, request));

        request = new LinkMarkFilterRequest();
        request.setInput("Descri");
        request.setDescription(true);
        argumentsList.add(Arguments.of(linkMarks, request));

        request = new LinkMarkFilterRequest();
        request.setInput("descript");
        request.setDescription(true);
        argumentsList.add(Arguments.of(linkMarks, request));

        request = new LinkMarkFilterRequest();
        request.setFrom(LocalDate.now().minusDays(2));
        argumentsList.add(Arguments.of(linkMarks, request));

        request = new LinkMarkFilterRequest();
        request.setTo(LocalDate.now().plusDays(1));
        argumentsList.add(Arguments.of(linkMarks, request));

        request = new LinkMarkFilterRequest();
        request.setFrom(LocalDate.now().minusDays(2));
        request.setTo(LocalDate.now().minusDays(1));
        argumentsList.add(Arguments.of(Collections.emptyList(), request));

        request = new LinkMarkFilterRequest();
        request.setTo(LocalDate.now().minusDays(1));
        argumentsList.add(Arguments.of(Collections.emptyList(), request));

        return argumentsList.stream();
    }
}
