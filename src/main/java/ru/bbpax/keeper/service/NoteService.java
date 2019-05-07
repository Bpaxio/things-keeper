package ru.bbpax.keeper.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.bbpax.keeper.model.Note;
import ru.bbpax.keeper.repo.NoteRepo;
import ru.bbpax.keeper.service.exception.NotFoundException;

import java.util.List;

import static ru.bbpax.keeper.model.NoteTypes.NOTE;

@Service
@AllArgsConstructor
public class NoteService {
    private final NoteRepo noteRepo;
    private final TagService tagService;

    public Note create(Note noteDto) {
        noteDto.setTags(tagService.updateTags(noteDto.getTags()));
        return noteRepo.save(noteDto);
    }

    public Note update(Note noteDto) {
        noteDto.setTags(tagService.updateTags(noteDto.getTags()));
        return noteRepo.save(noteDto);
    }

    public Note getById(String id) {
        return ((Note) noteRepo.findById(id)
                .orElseThrow(NotFoundException::new));
    }

    public List<Note> getAll() {
        return noteRepo.findAllByNoteType(NOTE);
    }

    public void deleteById(String id) {
        noteRepo.deleteById(id);
    }
}
