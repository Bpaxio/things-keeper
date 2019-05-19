package ru.bbpax.keeper.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.bbpax.keeper.model.Image;
import ru.bbpax.keeper.model.Recipe;
import ru.bbpax.keeper.model.Step;
import ru.bbpax.keeper.repo.recipe.RecipeRepo;
import ru.bbpax.keeper.rest.dto.ImageDto;
import ru.bbpax.keeper.rest.dto.RecipeDto;
import ru.bbpax.keeper.rest.request.RecipeFilterRequest;
import ru.bbpax.keeper.security.service.PrivilegeService;
import ru.bbpax.keeper.service.client.FilesClient;
import ru.bbpax.keeper.service.exception.NotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ru.bbpax.keeper.model.NoteTypes.RECIPE;
import static ru.bbpax.keeper.security.model.AccessLevels.OWN;

@Slf4j
@Service
@AllArgsConstructor
@Transactional(readOnly = true)
@PreAuthorize("hasAuthority('USER_ROLE')")
public class RecipeService {
    private final RecipeRepo repo;
    private final TagService tagService;
    private final FilterService filterService;
    private final FilesClient client;
    private final ModelMapper mapper;
    private final PrivilegeService privilegeService;

    @Transactional
    public RecipeDto create(RecipeDto dto) {
        dto.setId(null);
        final Recipe recipe = mapper.map(dto, Recipe.class);
        log.info("create: {}", recipe);
        recipe.setTags(tagService.updateTags(recipe.getTags()));
        final RecipeDto recipeDto = mapper.map(repo.save(recipe), RecipeDto.class);

        privilegeService.
                addPrivilege(recipeDto.getId(), OWN);
        log.info("saved: {}", recipeDto);
        return recipeDto;
    }

    @Transactional
    @PreAuthorize("hasWritePrivilege(#dto.id)")
    public RecipeDto update(RecipeDto dto) {
        final Recipe recipe = mapper.map(dto, Recipe.class);
        log.info("recipe: {}", recipe);
        recipe.setTags(tagService.updateTags(recipe.getTags()));
        dto = mapper.map(repo.save(recipe), RecipeDto.class);
        log.info("updated recipe: {}", dto);
        return dto;
    }

    @PreAuthorize("hasReadPrivilege(#id)")
    public RecipeDto getById(String id) {
        return repo.findById(id)
                .map(recipe -> mapper.map(recipe, RecipeDto.class))
                .orElseThrow(() -> new NotFoundException(RECIPE, id));
    }

    @PostFilter("hasReadPrivilege(filterObject.id)")
    public List<RecipeDto> getAll() {
        return repo.findAll()
                .stream()
                .map(recipe -> mapper.map(recipe, RecipeDto.class))
                .collect(Collectors.toList());
    }

    @PostFilter("hasReadPrivilege(filterObject.id)")
    public List<RecipeDto> getAll(RecipeFilterRequest request) {
        log.info("filterDTO: {}", request);
        return repo.findAll(filterService.makePredicate(request))
                .stream()
                .map(note -> mapper.map(note, RecipeDto.class))
                .collect(Collectors.toList());
    }

    @Transactional
    @PreAuthorize("hasDeletePrivilege(#id)")
    public void deleteById(String id) {
        repo.findById(id).ifPresent(recipe -> {
            if (recipe.getImage() != null && recipe.getImage().getLink() != null) {
                client.deleteFile(recipe.getImage().getLink());
            }
            recipe.getSteps().stream()
                    .map(Step::getImage)
                    .filter(image -> image != null && image.getLink() != null)
                    .forEach(image -> client.deleteFile(image.getLink()));
        });
        repo.deleteById(id);
    }

    @Transactional
    @PreAuthorize("hasWritePrivilege(#id)")
    public ImageDto uploadFile(String id, String imageId, MultipartFile file) {
        final Recipe recipe = repo.findById(id)
                .orElseThrow(() -> new NotFoundException(RECIPE, id));
        final Image image = findImageById(recipe, imageId)
                .orElseThrow(() -> new NotFoundException("Image", imageId));
        image.setOriginalName(file.getOriginalFilename());

        String link = image.getLink() == null
                ? client.saveFile(id, imageId, file)
                : client.updateFile(image.getLink(), file);
        image.setLink(link);

        repo.save(recipe);
        return mapper.map(image, ImageDto.class);
    }

    private Optional<Image> findImageById(Recipe recipe, String imageId) {
        if (recipe.getImage().getId().equals(imageId)) {
            return Optional.ofNullable(recipe.getImage());
        }
        return recipe.getSteps()
                .stream()
                .filter(step -> step.getImage().getId().equals(imageId))
                .findFirst()
                .map(Step::getImage);
    }
}
