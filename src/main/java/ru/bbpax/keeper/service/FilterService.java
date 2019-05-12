package ru.bbpax.keeper.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.bbpax.keeper.filter.LinkMarkFilter;
import ru.bbpax.keeper.filter.NoteFilter;
import ru.bbpax.keeper.filter.RecipeFilter;
import ru.bbpax.keeper.filter.core.DateIntervalFilter;
import ru.bbpax.keeper.filter.core.Filter;
import ru.bbpax.keeper.filter.core.TagFilter;
import ru.bbpax.keeper.filter.core.TitleFilter;
import ru.bbpax.keeper.model.Tag;
import ru.bbpax.keeper.repo.tag.TagRepo;
import ru.bbpax.keeper.rest.request.BaseFilterRequest;
import ru.bbpax.keeper.rest.request.LinkMarkFilterRequest;
import ru.bbpax.keeper.rest.request.NoteFilterRequest;
import ru.bbpax.keeper.rest.request.RecipeFilterRequest;

import static ru.bbpax.keeper.util.Helper.isBlank;

@Service
@AllArgsConstructor
public class FilterService {
    private final TagRepo tagRepo;

    public Filter parseFilter(NoteFilterRequest filterRequest) {
        return new NoteFilter()
                .with(new TitleFilter(filterRequest.getTitle()))
                .with(new DateIntervalFilter(filterRequest.getFrom(), filterRequest.getTo()))
                .with(new TagFilter(findTagForFilter(filterRequest)));
    }

    public Filter parseFilter(LinkMarkFilterRequest filterRequest) {
        return new LinkMarkFilter()
                .with(new TitleFilter(filterRequest.getTitle()))
                .with(new DateIntervalFilter(filterRequest.getFrom(), filterRequest.getTo()))
                .with(new TagFilter(findTagForFilter(filterRequest)));
    }

    public Filter parseFilter(RecipeFilterRequest filterRequest) {
        return new RecipeFilter()
                .with(new TitleFilter(filterRequest.getTitle()))
                .with(new DateIntervalFilter(filterRequest.getFrom(), filterRequest.getTo()))
                .with(new TagFilter(findTagForFilter(filterRequest)));
    }

    private <T extends BaseFilterRequest> Tag findTagForFilter(T filterRequest) {
        String tagName = filterRequest.getTag();
        if (isBlank(tagName)) return null;
        return tagRepo.findFirstByValue(tagName)
                .orElse(new Tag(tagName));
    }
}
