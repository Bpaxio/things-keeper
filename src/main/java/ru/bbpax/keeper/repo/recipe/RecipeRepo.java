package ru.bbpax.keeper.repo.recipe;

import org.springframework.stereotype.Repository;
import ru.bbpax.keeper.model.Recipe;
import ru.bbpax.keeper.repo.AbstractNoteRepo;

import java.util.List;

import static ru.bbpax.keeper.model.NoteTypes.RECIPE;

@Repository
public interface RecipeRepo extends AbstractNoteRepo<Recipe> {
    @Override
    default List<Recipe> findAll() {
        return findAllByNoteType(RECIPE);
    }
}
