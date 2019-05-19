package ru.bbpax.keeper.service;

import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreFilter;
import org.springframework.stereotype.Service;
import ru.bbpax.keeper.model.Tag;
import ru.bbpax.keeper.repo.tag.TagRepo;
import ru.bbpax.keeper.security.service.PrivilegeService;
import ru.bbpax.keeper.service.exception.NotFoundException;
import ru.bbpax.keeper.service.exception.TagIsUsedException;

import java.util.List;
import java.util.stream.Collectors;

import static ru.bbpax.keeper.security.model.AccessLevels.OWN;

@Service
@AllArgsConstructor
@PreAuthorize("hasAuthority('USER_ROLE')")
public class TagService {
    private final TagRepo repo;
    private final PrivilegeService privilegeService;

    public Tag create(Tag tag) {
        final Tag newTag = repo.save(tag);
        privilegeService.
                addPrivilege(newTag.getId(), OWN);
        return newTag;
    }

    @PreAuthorize("hasWritePrivilege(#dto.id)")
    public Tag update(Tag tag) {
        return repo.save(tag);
    }

    @PreAuthorize("hasReadPrivilege(#id)")
    public Tag getById(String id) {
        return repo.findById(id)
                .orElseThrow(() -> new NotFoundException("Tag", id));
    }

    @PostFilter("hasReadPrivilege(filterObject.id)")
    public List<Tag> getAllByValue(String name) {
        return repo.findAllByValue(name);
    }

    @PostFilter("hasReadPrivilege(filterObject.id)")
    public List<Tag> getAll() {
        return repo.findAll();
    }

    @PreAuthorize("hasDeletePrivilege(#id)")
    public void deleteById(String id) {
        Tag tag = repo.findById(id).orElseThrow(() -> new NotFoundException("Tag", id));
        if (repo.tagIsUsed(tag)) {
            throw new TagIsUsedException();
        }
        repo.deleteById(id);
    }

    @PreFilter("hasReadPrivilege(filterObject.id)")
    public List<Tag> updateTags(List<Tag> tags) {
        return tags.stream()
                .map(this::createIfMissing)
                .collect(Collectors.toList());
    }

    private Tag createIfMissing(Tag tag) {
        if (tag.getId() == null) {
            return repo.findFirstByValue(tag.getValue())
                    .orElse(create(tag));
        }
        return tag;
    }
}
