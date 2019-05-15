package ru.bbpax.keeper.service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import lombok.AllArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import ru.bbpax.keeper.filter.core.DateIntervalFilter;
import ru.bbpax.keeper.filter.core.Filter;
import ru.bbpax.keeper.filter.core.TagFilter;
import ru.bbpax.keeper.model.Tag;
import ru.bbpax.keeper.repo.tag.TagRepo;
import ru.bbpax.keeper.rest.request.BaseFilterRequest;
import ru.bbpax.keeper.rest.request.LinkMarkFilterRequest;
import ru.bbpax.keeper.rest.request.NoteFilterRequest;
import ru.bbpax.keeper.rest.request.RecipeFilterRequest;

import static ru.bbpax.keeper.model.NoteTypes.LINK_MARK;
import static ru.bbpax.keeper.model.NoteTypes.NOTE;
import static ru.bbpax.keeper.model.NoteTypes.RECIPE;
import static ru.bbpax.keeper.model.QLinkMark.linkMark;
import static ru.bbpax.keeper.model.QNote.note;
import static ru.bbpax.keeper.model.QRecipe.recipe;
import static ru.bbpax.keeper.util.Helper.isBlank;

@Service
@AllArgsConstructor
public class FilterService {
    private final TagRepo tagRepo;

    public Predicate makePredicate(@NonNull NoteFilterRequest request) {
        BooleanBuilder builder = new BooleanBuilder(note.noteType.eq(NOTE));

        if (request.getInput() != null) {
            BooleanBuilder stringExpression = new BooleanBuilder(note.title.containsIgnoreCase(request.getInput()));
            if (request.isDescription()) {
                stringExpression.or(note.description.containsIgnoreCase(request.getInput()));
            }
            builder.and(stringExpression.getValue());
        }

        configureFilters(builder, request);

        return builder.getValue();
    }

    public Predicate makePredicate(@NonNull LinkMarkFilterRequest request) {
        BooleanBuilder builder = new BooleanBuilder(linkMark.noteType.eq(LINK_MARK));

        if (request.getInput() != null) {
            BooleanBuilder stringExpression = new BooleanBuilder(linkMark.title.containsIgnoreCase(request.getInput()));
            if (request.isLink()) {
                stringExpression.or(linkMark.link.containsIgnoreCase(request.getInput()));
            }
            if (request.isDescription()) {
                stringExpression.or(linkMark.description.containsIgnoreCase(request.getInput()));
            }
            builder.and(stringExpression.getValue());
        }
        configureFilters(builder, request);

        return builder.getValue();
    }

    public Predicate makePredicate(@NonNull RecipeFilterRequest request) {
        BooleanBuilder builder = new BooleanBuilder(recipe.noteType.eq(RECIPE));

        if (request.getInput() != null) {
            BooleanBuilder stringExpression = new BooleanBuilder(recipe.title.containsIgnoreCase(request.getInput()));
            if (request.isLink()) {
                stringExpression.or(recipe.link.containsIgnoreCase(request.getInput()));
            }
            if (request.isIngredient()) {
                stringExpression.or(recipe.ingredients.any().name.containsIgnoreCase(request.getInput()));
            }
            builder.and(stringExpression.getValue());
        }

        configureFilters(builder, request);

        if (!isBlank(request.getCategory())) {
            builder.and(recipe.category.containsIgnoreCase(request.getCategory()));
        }

        return builder.getValue();
    }

    private <T extends BaseFilterRequest> void configureFilters(BooleanBuilder builder, T request) {
        Filter intervalFilter = new DateIntervalFilter(request.getFrom(), request.getTo());
        if (intervalFilter.isValid()) {
            builder.and(intervalFilter.toPredicate());
        }

        TagFilter tagFilter = new TagFilter(findTagForFilter(request));
        if (tagFilter.isValid()) {
            builder.and(tagFilter.toPredicate());
        }
    }

    private <T extends BaseFilterRequest> Tag findTagForFilter(T filterRequest) {
        String tagName = filterRequest.getTag();
        if (isBlank(tagName)) return null;
        return tagRepo.findFirstByValue(tagName)
                .orElse(new Tag(tagName));
    }
}
