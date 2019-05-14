package ru.bbpax.keeper.repo.recipe;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import ru.bbpax.keeper.model.Recipe;
import ru.bbpax.keeper.repo.AbstractNoteRepo;

import java.util.List;
import java.util.Optional;

import static ru.bbpax.keeper.model.NoteTypes.RECIPE;

@Repository
public interface RecipeRepo extends AbstractNoteRepo<Recipe> {

    @Override
    @NonNull
    default Optional<Recipe> findById(@NonNull String id) {
        return findByIdAndNoteType(id, RECIPE);
    }

    @Override
    default void deleteById(@NonNull String id) {
        deleteById(id, RECIPE);
    }

    @Override
    @NonNull
    default List<Recipe> findAll() {
        return findAllByNoteType(RECIPE);
    }

}
