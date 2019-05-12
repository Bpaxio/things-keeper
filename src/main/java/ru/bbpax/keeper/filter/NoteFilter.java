package ru.bbpax.keeper.filter;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import ru.bbpax.keeper.filter.core.CompositeFilter;
import ru.bbpax.keeper.filter.core.DateIntervalFilter;
import ru.bbpax.keeper.filter.core.Filter;
import ru.bbpax.keeper.filter.core.NoteTypeFilter;
import ru.bbpax.keeper.filter.core.TagFilter;
import ru.bbpax.keeper.filter.core.TitleFilter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static ru.bbpax.keeper.model.NoteTypes.NOTE;

public class NoteFilter implements CompositeFilter {
    private final NoteTypeFilter typeFilter;

    private TitleFilter title;
    private DateIntervalFilter date;
    private TagFilter tag;

    public NoteFilter(TitleFilter title, DateIntervalFilter date, TagFilter tag) {
        this();
        this.title = title;
        this.date = date;
        this.tag = tag;
    }

    public NoteFilter() {
        this(NOTE);
    }

    protected NoteFilter(String type) {
        typeFilter = new NoteTypeFilter(type);
    }

    @Override
    public List<Filter> filters() {
        return Arrays.asList(typeFilter, title, date, tag);
    }

    @Override
    public BooleanExpression toPredicate() {
        List<Filter> filters = filters();
        return Expressions.allOf(
                filters.stream()
                        .filter(Filter::isValid)
                        .map(Filter::toPredicate)
                        .collect(Collectors.toList())
                        .toArray(new BooleanExpression[filters.size()])
        );
    }

    @Override
    public Boolean isValid() {
        return !filters().isEmpty();
    }

    public NoteFilter with(TitleFilter title) {
        this.title = title;
        return this;
    }

    public NoteFilter with(DateIntervalFilter date) {
        this.date = date;
        return this;
    }

    public NoteFilter with(TagFilter tag) {
        this.tag = tag;
        return this;
    }
}
