package ru.bbpax.keeper.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.bbpax.keeper.model.LinkMark;
import ru.bbpax.keeper.repo.linkmark.LinkMarkRepo;
import ru.bbpax.keeper.rest.dto.LinkMarkDto;
import ru.bbpax.keeper.service.exception.NotFoundException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static ru.bbpax.keeper.util.EntityUtil.linkMarkDto;

@Slf4j
@SpringBootTest
@ExtendWith(SpringExtension.class)
class LinkMarkServiceTest {
    @Configuration
    @Import({ LinkMarkService.class, CommonConfiguration.class })
    static class Config {
    }
    @MockBean
    private LinkMarkRepo repo;
    @MockBean
    private TagService tagService;

    @Autowired
    private LinkMarkService service;
    @Autowired
    private ModelMapper mapper;

    @Test
    void create() {
        final LinkMarkDto dto = linkMarkDto();
        final LinkMark linkMark = mapper.map(dto, LinkMark.class);
        linkMark.setId(null);
        doReturn(mapper.map(dto, LinkMark.class)).when(repo).save(linkMark);
        doReturn(linkMark.getTags()).when(tagService).updateTags(linkMark.getTags());
        dto.setId("IdForCallSave");

        final LinkMarkDto result = service.create(dto);
        assertNotNull(result);
        assertEquals(dto.getInfo(), result.getInfo());
        assertNotNull(result.getId());
        assertNotEquals(dto.getId(), result.getId());

        verify(repo, times(1)).save(linkMark);
    }

    @Test
    void update() {
        final LinkMarkDto dto = linkMarkDto();
        final LinkMark linkMark = mapper.map(dto, LinkMark.class);
        doReturn(linkMark).when(repo).save(linkMark);
        doReturn(linkMark.getTags()).when(tagService).updateTags(linkMark.getTags());

        final LinkMarkDto result = service.update(dto);
        assertNotNull(result);
        assertEquals(dto, result);

        verify(repo, times(1)).save(linkMark);
    }

    @Test
    void getById() {
        final LinkMarkDto dto = linkMarkDto();
        final LinkMark linkMark = mapper.map(dto, LinkMark.class);
        doReturn(Optional.empty()).when(repo).findById(anyString());
        doReturn(Optional.of(linkMark)).when(repo).findById(dto.getId());

        final LinkMarkDto result = service.getById(dto.getId());
        assertNotNull(result);
        assertEquals(dto, result);
        assertThrows(NotFoundException.class, () -> service.getById("WRONG_ID"));

        verify(repo, times(1)).findById(linkMark.getId());
        verify(repo, times(1)).findById("WRONG_ID");
    }

    @Test
    void getAll() {
        List<LinkMarkDto> list = Arrays.asList(
                linkMarkDto(),
                linkMarkDto(),
                linkMarkDto(),
                linkMarkDto()
        );
        doReturn(
                list.stream()
                        .map(dto -> mapper.map(dto, LinkMark.class))
                        .collect(Collectors.toList())
        ).when(repo).findAll();

        final List<LinkMarkDto> all = service.getAll();
        assertNotNull(all);
        assertEquals(list, all);

        verify(repo, times(1)).findAll();
    }

    @Test
    void deleteById() {
        service.deleteById("ID");
        verify(repo, times(1)).deleteById("ID");
    }
}