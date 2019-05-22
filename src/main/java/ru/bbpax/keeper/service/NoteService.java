package ru.bbpax.keeper.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.bbpax.keeper.model.Note;
import ru.bbpax.keeper.repo.note.NoteRepo;
import ru.bbpax.keeper.rest.dto.NoteDto;
import ru.bbpax.keeper.rest.request.NoteFilterRequest;
import ru.bbpax.keeper.security.service.PrivilegeService;
import ru.bbpax.keeper.service.exception.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

import static ru.bbpax.keeper.model.NoteTypes.NOTE;
import static ru.bbpax.keeper.security.model.AccessLevels.OWN;

@Slf4j
@Service
@AllArgsConstructor
@Transactional(readOnly = true)
@PreAuthorize("hasAuthority('USER_ROLE')")
public class NoteService {
    private final NoteRepo repo;
    private final TagService tagService;
    private final FilterService filterService;
    private final ModelMapper mapper;
    private final PrivilegeService privilegeService;

    @Transactional
    @HystrixCommand(groupKey = "noteService", commandKey = "createNote")
    public NoteDto create(final NoteDto dto) {
        final Note note = mapper.map(dto, Note.class);
        note.setId(null);
        log.info("create: {}", note);
        note.setTags(tagService.updateTags(note.getTags()));
        final NoteDto noteDto = mapper.map(repo.save(note), NoteDto.class);

        privilegeService.
                addPrivilege(noteDto.getId(), OWN);

        log.info("saved: {}", noteDto);
        return noteDto;
    }

    @Transactional
    @PreAuthorize("hasWritePrivilege(#dto.id)")
    @HystrixCommand(groupKey = "noteService", commandKey = "updateNote")
    public NoteDto update(final NoteDto dto) {
        Note note = mapper.map(dto, Note.class);
        note.setTags(tagService.updateTags(note.getTags()));
        return mapper.map(repo.save(note), NoteDto.class);
    }

    @PreAuthorize("hasReadPrivilege(#id)")
    @HystrixCommand(groupKey = "noteService", commandKey = "getNoteById")
    public NoteDto getById(final String id) {
        return repo.findById(id)
                .map(note -> mapper.map(note, NoteDto.class))
                .orElseThrow(() -> new NotFoundException(NOTE, id));
    }

    @PostFilter("hasReadPrivilege(filterObject.id)")
    @HystrixCommand(groupKey = "noteService", commandKey = "getAllNote")
    public List<NoteDto> getAll(final NoteFilterRequest request) {
        log.info("filterDTO: {}", request);
        return repo.findAll(filterService.makePredicate(request))
                .stream()
                .map(note -> mapper.map(note, NoteDto.class))
                .collect(Collectors.toList());
    }

    @PostFilter("hasReadPrivilege(filterObject.id)")
    @HystrixCommand(groupKey = "noteService", commandKey = "getAllFilteredNote")
    public List<NoteDto> getAll() {
        return repo.findAll()
                .stream()
                .map(note -> mapper.map(note, NoteDto.class))
                .collect(Collectors.toList());
    }

    @Transactional
    @PreAuthorize("hasDeletePrivilege(#id)")
    @HystrixCommand(groupKey = "noteService", commandKey = "deleteNoteById")
    public void deleteById(final String id) {
        repo.deleteById(id);
    }
}
