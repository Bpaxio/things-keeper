package ru.bbpax.keeper.filter.core;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import lombok.Data;
import ru.bbpax.keeper.model.Tag;

import static ru.bbpax.keeper.model.QAbstractNote.abstractNote;

@Data
public class TagFilter implements Filter {
    private final Tag tag;

    public TagFilter(String tag) {
        this.tag = new Tag(tag);
    }

    public TagFilter(Tag tag) {
        this.tag = tag;
    }

    @Override
    public BooleanExpression toPredicate() {
        if (tag.getId() != null) {
            return abstractNote.tags.contains(this.tag);
        }
        return Expressions.asBoolean(false);
    }

    @Override
    public Boolean isValid() {
        return this.tag != null && tag.getValue() != null;
    }
}
