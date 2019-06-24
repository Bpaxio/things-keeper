package ru.bbpax.keeper.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.bbpax.keeper.rest.dto.IngredientDto;
import ru.bbpax.keeper.rest.dto.StepDto;
import ru.bbpax.keeper.service.RecipeService;
import ru.bbpax.keeper.rest.dto.RecipeDto;

import java.util.Collections;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.bbpax.keeper.util.EntityUtil.recipeDto;

/**
 * @author Vlad Rakhlinskii
 * Created on 08.05.2019.
 */
@Slf4j
@ExtendWith(SpringExtension.class)
@WebMvcTest
@WithMockUser
class RecipeControllerTest {
    @Configuration
    @Import({ RecipeController.class, MockMvcSecurityConfig.class })
    static class Config {
    }

    @Autowired
    private MockMvc mvc;
    @MockBean
    private RecipeService service;

    @Test
    void createRecipe() throws Exception {
        RecipeDto recipe = recipeDto();
        mvc.perform(post("/recipes/")
                .content(recipe.getDescription())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        mvc.perform(post("/recipes/")
                .content(new ObjectMapper().writeValueAsString(recipe))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(service, times(1)).create(recipe);
    }

    @Test
    void updateRecipe() throws Exception {
        RecipeDto recipe = recipeDto();

        mvc.perform(put("/recipes/" + recipe.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isMethodNotAllowed());

        MvcResult mvcResult = mvc.perform(put("/recipes/")
                .content(new ObjectMapper().writeValueAsString(recipe))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
        assertEquals(0, mvcResult.getResponse().getContentLength());

        verify(service, times(1)).update(recipe);
    }

    @Test
    void getRecipe() throws Exception {
        RecipeDto recipe = recipeDto();
        when(service.getById(recipe.getId()))
                .thenReturn(recipe);
        final StepDto expectedStep = recipe.getSteps().get(0);
        final IngredientDto expectedIngredient = recipe.getIngredients().get(0);
        mvc.perform(get("/recipes/" + recipe.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(recipe.getId())))
                .andExpect(jsonPath("$.description", is(recipe.getDescription())))
                .andExpect(jsonPath("$.tags", hasSize(3)))
                .andExpect(jsonPath("$.category", is(recipe.getCategory())))
                .andExpect(jsonPath("$.image.id", is(recipe.getImage().getId())))
                .andExpect(jsonPath("$.image.link", is(recipe.getImage().getLink())))
                .andExpect(jsonPath("$.image.originalName", is(recipe.getImage().getOriginalName())))
                .andExpect(jsonPath("$.steps", hasSize(3)))
                .andExpect(jsonPath("$.steps[0].id", is(expectedStep.getId())))
                .andExpect(jsonPath("$.steps[0].title", is(expectedStep.getTitle())))
                .andExpect(jsonPath("$.steps[0].description", is(expectedStep.getDescription())))
                .andExpect(jsonPath("$.steps[0].stepNumber", is(expectedStep.getStepNumber())))
                .andExpect(jsonPath("$.steps[0].image.id", is(expectedStep.getImage().getId())))
                .andExpect(jsonPath("$.steps[0].image.link", is(expectedStep.getImage().getLink())))
                .andExpect(jsonPath("$.steps[0].image.originalName", is(expectedStep.getImage().getOriginalName())))
                .andExpect(jsonPath("$.created", is(recipe.getCreated().toString())))
                .andExpect(jsonPath("$.ingredients", hasSize(3)))
                .andExpect(jsonPath("$.ingredients[0].id", is(expectedIngredient.getId())))
                .andExpect(jsonPath("$.ingredients[0].name", is(expectedIngredient.getName())))
                .andExpect(jsonPath("$.ingredients[0].value", is(expectedIngredient.getValue().toString())))
                .andExpect(jsonPath("$.ingredients[0].unit", is(expectedIngredient.getUnit())))
                .andExpect(jsonPath("$.link", is(recipe.getLink())))
                .andExpect(jsonPath("$.title", is(recipe.getTitle())))
                .andExpect(jsonPath("$.id", is(recipe.getId())));

        verify(service, times(1)).getById(recipe.getId());
    }

    @Test
    void getRecipes() throws Exception {
        RecipeDto recipe = recipeDto();
        when(service.getAll(any()))
                .thenReturn(Collections.singletonList(recipe));

        final StepDto expectedStep = recipe.getSteps().get(0);
        final IngredientDto expectedIngredient = recipe.getIngredients().get(0);
        mvc.perform(get("/recipes/")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(recipe.getId())))
                .andExpect(jsonPath("$[0].description", is(recipe.getDescription())))
                .andExpect(jsonPath("$[0].tags", hasSize(3)))
                .andExpect(jsonPath("$[0].category", is(recipe.getCategory())))
                .andExpect(jsonPath("$[0].image.id", is(recipe.getImage().getId())))
                .andExpect(jsonPath("$[0].image.link", is(recipe.getImage().getLink())))
                .andExpect(jsonPath("$[0].image.originalName", is(recipe.getImage().getOriginalName())))
                .andExpect(jsonPath("$[0].steps", hasSize(3)))
                .andExpect(jsonPath("$[0].steps[0].id", is(expectedStep.getId())))
                .andExpect(jsonPath("$[0].steps[0].title", is(expectedStep.getTitle())))
                .andExpect(jsonPath("$[0].steps[0].description", is(expectedStep.getDescription())))
                .andExpect(jsonPath("$[0].steps[0].stepNumber", is(expectedStep.getStepNumber())))
                .andExpect(jsonPath("$[0].steps[0].image.id", is(expectedStep.getImage().getId())))
                .andExpect(jsonPath("$[0].steps[0].image.link", is(expectedStep.getImage().getLink())))
                .andExpect(jsonPath("$[0].steps[0].image.originalName", is(expectedStep.getImage().getOriginalName())))
                .andExpect(jsonPath("$[0].created", is(recipe.getCreated().toString())))
                .andExpect(jsonPath("$[0].ingredients", hasSize(3)))
                .andExpect(jsonPath("$[0].ingredients[0].id", is(expectedIngredient.getId())))
                .andExpect(jsonPath("$[0].ingredients[0].name", is(expectedIngredient.getName())))
                .andExpect(jsonPath("$[0].ingredients[0].value", is(expectedIngredient.getValue().toString())))
                .andExpect(jsonPath("$[0].ingredients[0].unit", is(expectedIngredient.getUnit())))
                .andExpect(jsonPath("$[0].link", is(recipe.getLink())))
                .andExpect(jsonPath("$[0].created", is(recipe.getCreated().toString())))
                .andExpect(jsonPath("$[0].title", is(recipe.getTitle())))
                .andExpect(jsonPath("$[0].id", is(recipe.getId())));


        verify(service, times(1)).getAll(any());
    }

    @Test
    void deleteRecipeById() throws Exception {
        RecipeDto recipe = recipeDto();
        mvc.perform(delete("/recipes/")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isMethodNotAllowed());

        mvc.perform(delete("/recipes/" + recipe.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(service, times(1)).deleteById(recipe.getId());
    }
}