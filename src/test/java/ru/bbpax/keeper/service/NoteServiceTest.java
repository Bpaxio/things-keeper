package ru.bbpax.keeper.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.bbpax.keeper.model.Note;
import ru.bbpax.keeper.repo.note.NoteRepo;
import ru.bbpax.keeper.rest.dto.NoteDto;
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
import static ru.bbpax.keeper.util.EntityUtil.noteDto;

@Slf4j
@SpringBootTest
@ExtendWith(SpringExtension.class)
class NoteServiceTest {
    @Configuration
    @Import({ NoteService.class })
    static class Config {
        @Bean
        public ModelMapper mapper() {
            return new ModelMapper();
        }
    }
    @MockBean
    private NoteRepo repo;
    @MockBean
    private TagService tagService;

    @Autowired
    private NoteService service;
    @Autowired
    private ModelMapper mapper;

    @Test
    void create() {
        final NoteDto dto = noteDto();
        final Note note = mapper.map(dto, Note.class);
        note.setId(null);
        doReturn(mapper.map(dto, Note.class)).when(repo).save(note);
        doReturn(note.getTags()).when(tagService).updateTags(note.getTags());
        dto.setId("IdForCallSave");

        final NoteDto result = service.create(dto);
        assertNotNull(result);
        assertEquals(dto.getInfo(), result.getInfo());
        assertNotNull(result.getId());
        assertNotEquals(dto.getId(), result.getId());

        verify(repo, times(1)).save(note);
    }

    @Test
    void update() {
        final NoteDto dto = noteDto();
        final Note note = mapper.map(dto, Note.class);
        doReturn(note).when(repo).save(note);
        doReturn(note.getTags()).when(tagService).updateTags(note.getTags());

        final NoteDto result = service.update(dto);
        assertNotNull(result);
        assertEquals(dto, result);

        verify(repo, times(1)).save(note);
    }

    @Test
    void getById() {
        final NoteDto dto = noteDto();
        final Note note = mapper.map(dto, Note.class);
        doReturn(Optional.empty()).when(repo).findById(anyString());
        doReturn(Optional.of(note)).when(repo).findById(dto.getId());

        final NoteDto result = service.getById(dto.getId());
        assertNotNull(result);
        assertEquals(dto, result);
        assertThrows(NotFoundException.class, () -> service.getById("WRONG_ID"));

        verify(repo, times(1)).findById(note.getId());
        verify(repo, times(1)).findById("WRONG_ID");
    }

    @Test
    void getAll() {
        List<NoteDto> list = Arrays.asList(
                noteDto(),
                noteDto(),
                noteDto(),
                noteDto()
        );
        doReturn(
                list.stream()
                        .map(dto -> mapper.map(dto, Note.class))
                        .collect(Collectors.toList())
        ).when(repo).findAll();

        final List<NoteDto> all = service.getAll();
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