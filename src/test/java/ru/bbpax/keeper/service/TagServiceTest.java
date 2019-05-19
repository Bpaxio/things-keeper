package ru.bbpax.keeper.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.bbpax.keeper.model.Tag;
import ru.bbpax.keeper.repo.tag.TagRepo;
import ru.bbpax.keeper.security.service.PrivilegeService;
import ru.bbpax.keeper.service.exception.NotFoundException;
import ru.bbpax.keeper.service.exception.TagIsUsedException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static ru.bbpax.keeper.util.EntityUtil.tag;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class TagServiceTest {

    @Configuration
    @Import({ TagService.class })
    static class Config {

    }
    @MockBean
    private TagRepo repo;
    @MockBean
    private PrivilegeService privilegeService;

    @Autowired
    private TagService service;

    @Test
    void updateTags() {
        List<Tag> list = Arrays.asList(
                tag("old 1"),
                new Tag("new 2"),
                tag("old 3"),
                new Tag("new 4")
        );
        doReturn(Optional.empty()).when(repo).findById(anyString());
        doReturn(Optional.of(list.get(0))).when(repo).findById(list.get(0).getId());
        doReturn(Optional.of(list.get(2))).when(repo).findById(list.get(2).getId());

        doReturn(tag(list.get(1).getValue())).when(repo).save(list.get(1));
        doReturn(tag(list.get(3).getValue())).when(repo).save(list.get(3));

        final List<Tag> tags = service.updateTags(list);
        assertThat(tags)
                .isNotNull()
                .isNotEmpty()
                .hasSize(list.size())
                .contains(list.get(0), list.get(2));

        assertEquals(list.get(1).getValue(), tags.get(1).getValue());
        assertNotEquals(list.get(1).getId(), tags.get(1).getId());
        assertEquals(list.get(3).getValue(), tags.get(3).getValue());
        assertNotEquals(list.get(3).getId(), tags.get(3).getId());
    }

    @Test
    void create() {
        final Tag tagByName = new Tag("by name only");
        final Tag tagByAll = tag("Name");

        doReturn(tag(tagByName.getValue())).when(repo).save(tagByName);
        doReturn(tag(tagByAll.getValue())).when(repo).save(tagByAll);

        final Tag resultByName = service.create(tagByName);
        assertNotNull(resultByName);
        assertEquals(tagByName.getValue(), resultByName.getValue());
        assertNotNull(resultByName.getId());

        final Tag resultByAll = service.create(tagByAll);
        assertNotNull(resultByAll);
        assertEquals(tagByAll.getValue(), resultByAll.getValue());
        assertNotEquals(tagByAll.getId(), resultByAll.getId());

    }

    @Test
    void update() {
        final Tag tagByName = new Tag("by name only");
        final Tag tagByAll = tag("Name");

        doReturn(tag(tagByName.getValue())).when(repo).save(tagByName);
        doReturn(tagByAll).when(repo).save(tagByAll);

        final Tag resultByName = service.update(tagByName);
        assertNotNull(resultByName);
        assertEquals(tagByName.getValue(), resultByName.getValue());
        assertNotNull(resultByName.getId());

        final Tag resultByAll = service.update(tagByAll);
        assertNotNull(resultByAll);
        assertEquals(tagByAll.getValue(), resultByAll.getValue());
        assertEquals(tagByAll.getId(), resultByAll.getId());
    }

    @Test
    void getById() {
        final Tag tag = tag("Name");
        doReturn(Optional.empty()).when(repo).findById(anyString());
        doReturn(Optional.of(tag)).when(repo).findById(tag.getId());

        final Tag byId = service.getById(tag.getId());
        assertNotNull(byId);
        assertEquals(tag.getValue(), byId.getValue());
        assertEquals(tag.getId(), byId.getId());
        assertEquals(tag, byId);
        assertThrows(NotFoundException.class, () -> service.getById("WRONG_ID"));
    }

    @Test
    void getAll() {
        List<Tag> list = Arrays.asList(
                tag("1"),
                tag("2"),
                tag("3"),
                tag("4")
        );
        doReturn(list).when(repo).findAll();

        final List<Tag> all = service.getAll();
        assertNotNull(all);
        assertEquals(list, all);
    }

    @Test
    void deleteById() {
        final Tag unusedTag = tag();
        final Tag usedTag = tag("First");
        doReturn(Optional.of(unusedTag)).when(repo).findById(unusedTag.getId());
        doReturn(Optional.of(usedTag)).when(repo).findById(usedTag.getId());
        doReturn(false).when(repo).tagIsUsed(unusedTag);
        doReturn(true).when(repo).tagIsUsed(usedTag);

        service.deleteById(unusedTag.getId());
        assertThrows(TagIsUsedException.class, () -> service.deleteById(usedTag.getId()));

        verify(repo, times(1)).tagIsUsed(unusedTag);
        verify(repo, times(1)).tagIsUsed(usedTag);
        verify(repo, times(1)).findById(unusedTag.getId());
        verify(repo, times(1)).findById(usedTag.getId());
        verify(repo, times(1)).deleteById(unusedTag.getId());
        verify(repo, times(0)).deleteById(usedTag.getId());
    }

    @Test
    void getAllByValue() {
        final List<Tag> first = Arrays.asList(tag("first"),
                tag("first1"),
                tag("firstsafv"),
                tag("gbfirst"),
                tag("firstdfsb"),
                tag("sfgb_first"),
                tag("first gsb"),
                tag("dfb first")
        );
        doReturn(first).when(repo).findAllByValue("first");

        assertEquals(first, service.getAllByValue("first"));
    }
}