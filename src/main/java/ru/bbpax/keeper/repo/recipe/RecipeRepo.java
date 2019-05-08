package ru.bbpax.keeper.repo.recipe;

import org.springframework.stereotype.Repository;
import ru.bbpax.keeper.model.Recipe;
import ru.bbpax.keeper.repo.AbstractNoteRepo;

@Repository
public interface RecipeRepo extends AbstractNoteRepo<Recipe> {
}
