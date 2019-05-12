package ru.bbpax.keeper.filter.core;

import com.querydsl.core.types.dsl.BooleanExpression;

import static ru.bbpax.keeper.model.QAbstractNote.abstractNote;

public class NoteTypeFilter implements Filter {
    private final String type;

    public NoteTypeFilter(String type) {
        this.type = type;
    }

    @Override
    public BooleanExpression toPredicate() {
        return abstractNote.noteType.eq(type);
    }

    @Override
    public Boolean isValid() {
        return type != null;
    }
}
