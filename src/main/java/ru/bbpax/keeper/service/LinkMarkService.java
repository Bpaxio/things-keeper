package ru.bbpax.keeper.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.bbpax.keeper.model.LinkMark;
import ru.bbpax.keeper.repo.linkmark.LinkMarkRepo;
import ru.bbpax.keeper.service.dto.LinkMarkDto;
import ru.bbpax.keeper.service.exception.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class LinkMarkService {
    private final LinkMarkRepo repo;
    private final TagService tagService;
    private final ModelMapper mapper;

    @Transactional
    public LinkMarkDto create(LinkMarkDto dto) {
        dto.setId(null);
        LinkMark linkMark = mapper.map(dto, LinkMark.class);
        log.info("create: {}", linkMark);
        linkMark.setTags(tagService.updateTags(linkMark.getTags()));
        final LinkMarkDto linkMarkDto = mapper.map(repo.save(linkMark), LinkMarkDto.class);
        log.info("saved: {}", linkMarkDto);
        return linkMarkDto;
    }

    @Transactional
    public LinkMarkDto update(LinkMarkDto dto) {
        LinkMark linkMark = mapper.map(dto, LinkMark.class);
        linkMark.setTags(tagService.updateTags(linkMark.getTags()));
        return mapper.map(repo.save(linkMark), LinkMarkDto.class);
    }

    public LinkMarkDto getById(String id) {
        return repo.findById(id)
                .map(linkMark -> mapper.map(linkMark, LinkMarkDto.class))
                .orElseThrow(NotFoundException::new);
    }

    public List<LinkMarkDto> getAll() {
        return repo.findAll()
                .stream()
                .map(linkMark -> mapper.map(linkMark, LinkMarkDto.class))
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteById(String id) {
        repo.deleteById(id);
    }
}
