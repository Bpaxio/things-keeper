package ru.bbpax.keeper.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.bbpax.keeper.model.LinkMark;
import ru.bbpax.keeper.repo.NoteRepo;
import ru.bbpax.keeper.service.exception.NotFoundException;

import java.util.List;

import static ru.bbpax.keeper.model.NoteTypes.LINK_MARK;

@Service
@AllArgsConstructor
public class LinkMarkService {

    private final NoteRepo noteRepo;
    private final TagService tagService;

    public LinkMark create(LinkMark noteDto) {
        noteDto.setTags(tagService.updateTags(noteDto.getTags()));
        return noteRepo.save(noteDto);
    }

    public LinkMark update(LinkMark noteDto) {
        noteDto.setTags(tagService.updateTags(noteDto.getTags()));
        return noteRepo.save(noteDto);
    }

    public LinkMark getById(String id) {
        return ((LinkMark) noteRepo.findById(id)
                .orElseThrow(NotFoundException::new));
    }

    public List<LinkMark> getAll() {
        return noteRepo.findAllByNoteType(LINK_MARK);
    }

    public void deleteById(String id) {
        noteRepo.deleteById(id);
    }
}
