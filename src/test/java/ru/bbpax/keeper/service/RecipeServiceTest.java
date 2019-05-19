package ru.bbpax.keeper.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.bbpax.keeper.model.Recipe;
import ru.bbpax.keeper.repo.recipe.RecipeRepo;
import ru.bbpax.keeper.rest.dto.RecipeDto;
import ru.bbpax.keeper.service.client.FilesClient;
import ru.bbpax.keeper.service.exception.NotFoundException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static ru.bbpax.keeper.util.EntityUtil.recipeDto;

@Slf4j
@SpringBootTest
@ExtendWith(SpringExtension.class)
class RecipeServiceTest {

    @Configuration
    @Import({ RecipeService.class, CommonConfiguration.class })
    static class Config {
    }
    @MockBean
    private RecipeRepo repo;
    @MockBean
    private TagService tagService;
    @MockBean
    private FilesClient client;

    @Autowired
    private RecipeService service;
    @Autowired
    private ModelMapper mapper;

    @Test
    void create() {
        final RecipeDto dto = recipeDto();
        final Recipe recipe = mapper.map(dto, Recipe.class);
        recipe.setId(null);
        doReturn(mapper.map(dto, Recipe.class)).when(repo).save(recipe);
        doReturn(recipe.getTags()).when(tagService).updateTags(recipe.getTags());
        dto.setId("IdForCallSave");

        final RecipeDto result = service.create(dto);
        assertNotNull(result);
        assertEquals(dto.getInfo(), result.getInfo());
        assertNotNull(result.getId());
        assertNotEquals(dto.getId(), result.getId());

        verify(repo, times(1)).save(recipe);
    }

    @Test
    void update() {
        final RecipeDto dto = recipeDto();
        final Recipe recipe = mapper.map(dto, Recipe.class);
        doReturn(recipe).when(repo).save(recipe);
        doReturn(recipe.getTags()).when(tagService).updateTags(recipe.getTags());

        final RecipeDto result = service.update(dto);
        assertNotNull(result);
        assertEquals(dto, result);

        verify(repo, times(1)).save(recipe);
    }

    @Test
    void getById() {
        final RecipeDto dto = recipeDto();
        final Recipe recipe = mapper.map(dto, Recipe.class);
        doReturn(Optional.empty()).when(repo).findById(anyString());
        doReturn(Optional.of(recipe)).when(repo).findById(dto.getId());

        final RecipeDto result = service.getById(dto.getId());
        assertNotNull(result);
        assertEquals(dto, result);
        assertThrows(NotFoundException.class, () -> service.getById("WRONG_ID"));

        verify(repo, times(1)).findById(recipe.getId());
        verify(repo, times(1)).findById("WRONG_ID");
    }

    @Test
    void getAll() {
        List<RecipeDto> list = Arrays.asList(
                recipeDto(),
                recipeDto(),
                recipeDto(),
                recipeDto()
        );
        doReturn(
                list.stream()
                        .map(dto -> mapper.map(dto, Recipe.class))
                        .collect(Collectors.toList())
        ).when(repo).findAll();

        final List<RecipeDto> all = service.getAll();
        assertNotNull(all);
        assertEquals(list, all);

        verify(repo, times(1)).findAll();
    }

    @Test
    @Disabled
    void getAllWithFilter() {
    }

    @Test
    void deleteById() {
        service.deleteById("ID");
        verify(repo, times(1)).deleteById("ID");
    }

    @Test
    @Disabled
    void uploadFile() {
    }
}