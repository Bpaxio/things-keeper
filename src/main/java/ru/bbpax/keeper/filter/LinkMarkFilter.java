package ru.bbpax.keeper.filter;

import ru.bbpax.keeper.filter.core.CompositeFilter;

import static ru.bbpax.keeper.model.NoteTypes.LINK_MARK;

public class LinkMarkFilter extends NoteFilter implements CompositeFilter {
    public LinkMarkFilter() {
        super(LINK_MARK);
    }
}
