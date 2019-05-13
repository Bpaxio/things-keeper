package ru.bbpax.keeper.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.bbpax.keeper.model.Recipe;
import ru.bbpax.keeper.repo.recipe.RecipeRepo;
import ru.bbpax.keeper.rest.dto.RecipeDto;
import ru.bbpax.keeper.rest.request.RecipeFilterRequest;
import ru.bbpax.keeper.service.exception.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class RecipeService {
    private final RecipeRepo repo;
    private final TagService tagService;
    private final FilterService filterService;
    private final ModelMapper mapper;

    @Transactional
    public RecipeDto create(RecipeDto dto) {
        dto.setId(null);
        final Recipe recipe = mapper.map(dto, Recipe.class);
        log.info("create: {}", recipe);
        recipe.setTags(tagService.updateTags(recipe.getTags()));
        final RecipeDto recipeDto = mapper.map(repo.save(recipe), RecipeDto.class);
        log.info("saved: {}", recipeDto);
        return recipeDto;
    }

    @Transactional
    public RecipeDto update(RecipeDto dto) {
        final Recipe recipe = mapper.map(dto, Recipe.class);
        log.info("recipe: {}", recipe);
        recipe.setTags(tagService.updateTags(recipe.getTags()));
        dto = mapper.map(repo.save(recipe), RecipeDto.class);
        log.info("updated recipe: {}", dto);
        return dto;
    }

    public RecipeDto getById(String id) {
        return repo.findById(id)
                .map(recipe -> mapper.map(recipe, RecipeDto.class))
                .orElseThrow(NotFoundException::new);
    }

    public List<RecipeDto> getAll() {
        return repo.findAll()
                .stream()
                .map(recipe -> mapper.map(recipe, RecipeDto.class))
                .collect(Collectors.toList());
    }

    public List<RecipeDto> getAll(RecipeFilterRequest request) {
        log.info("filterDTO: {}", request);
        return repo.findAll(filterService.makePredicate(request))
                .stream()
                .map(note -> mapper.map(note, RecipeDto.class))
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteById(String id) {
        repo.deleteById(id);
    }
}
