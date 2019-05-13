package ru.bbpax.keeper.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.bbpax.keeper.model.Note;
import ru.bbpax.keeper.repo.note.NoteRepo;
import ru.bbpax.keeper.rest.dto.NoteDto;
import ru.bbpax.keeper.rest.request.NoteFilterRequest;
import ru.bbpax.keeper.service.exception.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class NoteService {
    private final NoteRepo repo;
    private final TagService tagService;
    private final FilterService filterService;
    private final ModelMapper mapper;

    @Transactional
    public NoteDto create(final NoteDto dto) {
        final Note note = mapper.map(dto, Note.class);
        note.setId(null);
        log.info("create: {}", note);
        note.setTags(tagService.updateTags(note.getTags()));
        final NoteDto noteDto = mapper.map(repo.save(note), NoteDto.class);
        log.info("saved: {}", noteDto);
        return noteDto;
    }

    @Transactional
    public NoteDto update(NoteDto dto) {
        Note note = mapper.map(dto, Note.class);
        note.setTags(tagService.updateTags(note.getTags()));
        return mapper.map(repo.save(note), NoteDto.class);
    }

    public NoteDto getById(String id) {
        return repo.findById(id)
                .map(note -> mapper.map(note, NoteDto.class))
                .orElseThrow(NotFoundException::new);
    }

    public List<NoteDto> getAll(NoteFilterRequest request) {
        log.info("filterDTO: {}", request);
        return repo.findAll(filterService.makePredicate(request))
                .stream()
                .map(note -> mapper.map(note, NoteDto.class))
                .collect(Collectors.toList());
    }

    public List<NoteDto> getAll() {
        return repo.findAll()
                .stream()
                .map(note -> mapper.map(note, NoteDto.class))
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteById(String id) {
        repo.deleteById(id);
    }
}
