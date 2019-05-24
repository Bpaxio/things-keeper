package ru.bbpax.keeper.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.bbpax.keeper.model.LinkMark;
import ru.bbpax.keeper.repo.linkmark.LinkMarkRepo;
import ru.bbpax.keeper.rest.dto.LinkMarkDto;
import ru.bbpax.keeper.rest.request.LinkMarkFilterRequest;
import ru.bbpax.keeper.security.service.PrivilegeService;
import ru.bbpax.keeper.service.exception.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

import static ru.bbpax.keeper.model.NoteTypes.LINK_MARK;
import static ru.bbpax.keeper.security.model.AccessLevels.OWN;

@Slf4j
@Service
@AllArgsConstructor
@Transactional(readOnly = true)
@PreAuthorize("hasAuthority('USER_ROLE')")
public class LinkMarkService {
    private final LinkMarkRepo repo;
    private final TagService tagService;
    private final FilterService filterService;
    private final ModelMapper mapper;
    private final PrivilegeService privilegeService;

    @Transactional
    public LinkMarkDto create(LinkMarkDto dto) {
        dto.setId(null);
        LinkMark linkMark = mapper.map(dto, LinkMark.class);
        log.info("create: {}", linkMark);

        if (linkMark.getTags() != null)
            linkMark.setTags(tagService.updateTags(linkMark.getTags()));
        final LinkMarkDto linkMarkDto = mapper.map(repo.save(linkMark), LinkMarkDto.class);

        privilegeService.
                addPrivilege(linkMarkDto.getId(), OWN);
        log.info("saved: {}", linkMarkDto);
        return linkMarkDto;
    }

    @Transactional
    @PreAuthorize("hasWritePrivilege(#dto.id)")
    public LinkMarkDto update(LinkMarkDto dto) {
        LinkMark linkMark = mapper.map(dto, LinkMark.class);

        if (linkMark.getTags() != null)
            linkMark.setTags(tagService.updateTags(linkMark.getTags()));
        return mapper.map(repo.save(linkMark), LinkMarkDto.class);
    }

    @PreAuthorize("hasReadPrivilege(#id)")
    public LinkMarkDto getById(String id) {
        return repo.findById(id)
                .map(linkMark -> mapper.map(linkMark, LinkMarkDto.class))
                .orElseThrow(() -> new NotFoundException(LINK_MARK, id));
    }

    @PostFilter("hasReadPrivilege(filterObject.id)")
    public List<LinkMarkDto> getAll() {
        return repo.findAll()
                .stream()
                .map(linkMark -> mapper.map(linkMark, LinkMarkDto.class))
                .collect(Collectors.toList());
    }

    @PostFilter("hasReadPrivilege(filterObject.id)")
    public List<LinkMarkDto> getAll(LinkMarkFilterRequest request) {
        log.info("filterDTO: {}", request);
        return repo.findAll(filterService.makePredicate(request))
                .stream()
                .map(note -> mapper.map(note, LinkMarkDto.class))
                .collect(Collectors.toList());
    }

    @Transactional
    @PreAuthorize("hasDeletePrivilege(#id)")
    public void deleteById(String id) {
        repo.deleteById(id);
    }
}
