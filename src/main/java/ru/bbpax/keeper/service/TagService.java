package ru.bbpax.keeper.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.bbpax.keeper.model.Tag;
import ru.bbpax.keeper.repo.tag.TagRepo;
import ru.bbpax.keeper.service.exception.NotFoundException;
import ru.bbpax.keeper.service.exception.TagIsUsedException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TagService {
    private final TagRepo repo;

    public List<Tag> updateTags(List<Tag> tags) {
        return tags.stream()
                .map(this::createIfMissing)
                .collect(Collectors.toList());
    }

    public Tag create(Tag tag) {
        return repo.save(tag);
    }

    public Tag update(Tag tag) {
        return repo.save(tag);
    }

    public Tag getById(String id) {
        return repo.findById(id)
                .orElseThrow(NotFoundException::new);
    }

    public List<Tag> getAll() {
        return repo.findAll();
    }

    public void deleteById(String id) {
        Tag tag = repo.findById(id).orElseThrow(NotFoundException::new);
        if (repo.tagIsUsed(tag)) {
            throw new TagIsUsedException();
        }
        repo.deleteById(id);
    }

    private Tag createIfMissing(Tag tag) {
        if (tag.getId() == null) {
            return create(tag);
        }
        return tag;
    }
}
