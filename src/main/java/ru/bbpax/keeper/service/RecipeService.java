package ru.bbpax.keeper.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.bbpax.keeper.model.Recipe;
import ru.bbpax.keeper.repo.NoteRepo;
import ru.bbpax.keeper.service.exception.NotFoundException;

import java.util.List;

import static ru.bbpax.keeper.model.NoteTypes.RECIPE;

@Service
@AllArgsConstructor
public class RecipeService {
    private final NoteRepo noteRepo;
    private final TagService tagService;

    public Recipe create(Recipe noteDto) {
        noteDto.setTags(tagService.updateTags(noteDto.getTags()));
        return noteRepo.save(noteDto);
    }

    public Recipe update(Recipe noteDto) {
        noteDto.setTags(tagService.updateTags(noteDto.getTags()));
        return noteRepo.save(noteDto);
    }

    public Recipe getById(String id) {
        return ((Recipe) noteRepo.findById(id)
                .orElseThrow(NotFoundException::new));
    }

    public List<Recipe> getAll() {
        return noteRepo.findAllByNoteType(RECIPE);
    }

    public void deleteById(String id) {
        noteRepo.deleteById(id);
    }
}
