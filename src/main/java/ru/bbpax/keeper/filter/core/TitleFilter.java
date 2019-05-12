package ru.bbpax.keeper.filter.core;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.Data;

import static ru.bbpax.keeper.model.QAbstractNote.abstractNote;

@Data
public class TitleFilter implements Filter {
    private final String title;

    public TitleFilter(String title) {
        this.title = title;
    }

    @Override
    public BooleanExpression toPredicate() {
        return abstractNote.title.contains(isValid() ? this.title : "");
    }

    @Override
    public Boolean isValid() {
        return title != null;
    }
}
