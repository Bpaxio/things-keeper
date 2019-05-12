package ru.bbpax.keeper.filter;

import ru.bbpax.keeper.filter.core.CompositeFilter;

import static ru.bbpax.keeper.model.NoteTypes.RECIPE;

public class RecipeFilter extends NoteFilter implements CompositeFilter {
    public RecipeFilter() {
        super(RECIPE);
    }
}
