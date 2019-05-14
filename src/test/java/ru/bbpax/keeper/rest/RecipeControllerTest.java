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
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.bbpax.keeper.service.RecipeService;
import ru.bbpax.keeper.rest.dto.RecipeDto;

import java.util.Collections;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
class RecipeControllerTest {
    @Configuration
    @Import({ RecipeController.class })
    static class Config {
    }

    @Autowired
    private MockMvc mvc;
    @MockBean
    private RecipeService service;

    @Test
    void createRecipe() throws Exception {
        RecipeDto recipe = recipeDto();
        mvc.perform(post("/api/v1/recipes/")
                .content(recipe.getDescription())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        mvc.perform(post("/api/v1/recipes/")
                .content(new ObjectMapper().writeValueAsString(recipe))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(service, times(1)).create(recipe);
    }

    @Test
    void updateRecipe() throws Exception {
        RecipeDto recipe = recipeDto();

        mvc.perform(put("/api/v1/recipes/" + recipe.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isMethodNotAllowed());

        MvcResult mvcResult = mvc.perform(put("/api/v1/recipes/")
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

        ObjectMapper mapper = new ObjectMapper();

        mvc.perform(get("/api/v1/recipes/" + recipe.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(recipe.getId())))
                .andExpect(jsonPath("$.description", is(recipe.getDescription())))
                .andExpect(jsonPath("$.tags", hasSize(3)))
                .andExpect(jsonPath("$.category", is(recipe.getCategory())))
                .andExpect(jsonPath("$.image", is(recipe.getImage())))
                .andExpect(jsonPath("$.steps", hasSize(3)))
                .andExpect(jsonPath("$.steps[0].id", is(recipe.getSteps().get(0).getId())))
                .andExpect(jsonPath("$.steps[0].input", is(recipe.getSteps().get(0).getTitle())))
                .andExpect(jsonPath("$.steps[0].description", is(recipe.getSteps().get(0).getDescription())))
                .andExpect(jsonPath("$.steps[0].stepNumber", is(recipe.getSteps().get(0).getStepNumber())))
                .andExpect(jsonPath("$.steps[0].image", is(recipe.getSteps().get(0).getImage())))
                .andExpect(jsonPath("$.created", is(recipe.getCreated().toString())))
                .andExpect(jsonPath("$.ingredients", hasSize(3)))
                .andExpect(jsonPath("$.ingredients[0].id", is(recipe.getIngredients().get(0).getId())))
                .andExpect(jsonPath("$.ingredients[0].name", is(recipe.getIngredients().get(0).getName())))
                .andExpect(jsonPath("$.ingredients[0].value", is(recipe.getIngredients().get(0).getValue().toString())))
                .andExpect(jsonPath("$.ingredients[0].unit", is(recipe.getIngredients().get(0).getUnit())))
                .andExpect(jsonPath("$.link", is(recipe.getLink())))
                .andExpect(jsonPath("$.input", is(recipe.getTitle())))
                .andExpect(jsonPath("$.id", is(recipe.getId())));

        verify(service, times(1)).getById(recipe.getId());
    }

    @Test
    void getRecipes() throws Exception {
        RecipeDto recipe = recipeDto();
        when(service.getAll())
                .thenReturn(Collections.singletonList(recipe));

        ObjectMapper mapper = new ObjectMapper();
        mvc.perform(get("/api/v1/recipes/")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(recipe.getId())))
                .andExpect(jsonPath("$[0].description", is(recipe.getDescription())))
                .andExpect(jsonPath("$[0].tags", hasSize(3)))
                .andExpect(jsonPath("$[0].category", is(recipe.getCategory())))
                .andExpect(jsonPath("$[0].image", is(recipe.getImage())))
                .andExpect(jsonPath("$[0].steps", hasSize(3)))
                .andExpect(jsonPath("$[0].steps[0].id", is(recipe.getSteps().get(0).getId())))
                .andExpect(jsonPath("$[0].steps[0].input", is(recipe.getSteps().get(0).getTitle())))
                .andExpect(jsonPath("$[0].steps[0].description", is(recipe.getSteps().get(0).getDescription())))
                .andExpect(jsonPath("$[0].steps[0].stepNumber", is(recipe.getSteps().get(0).getStepNumber())))
                .andExpect(jsonPath("$[0].steps[0].image", is(recipe.getSteps().get(0).getImage())))
                .andExpect(jsonPath("$[0].created", is(recipe.getCreated().toString())))
                .andExpect(jsonPath("$[0].ingredients", hasSize(3)))
                .andExpect(jsonPath("$[0].ingredients[0].id", is(recipe.getIngredients().get(0).getId())))
                .andExpect(jsonPath("$[0].ingredients[0].name", is(recipe.getIngredients().get(0).getName())))
                .andExpect(jsonPath("$[0].ingredients[0].value", is(recipe.getIngredients().get(0).getValue().toString())))
                .andExpect(jsonPath("$[0].ingredients[0].unit", is(recipe.getIngredients().get(0).getUnit())))
                .andExpect(jsonPath("$[0].link", is(recipe.getLink())))
                .andExpect(jsonPath("$[0].created", is(recipe.getCreated().toString())))
                .andExpect(jsonPath("$[0].input", is(recipe.getTitle())))
                .andExpect(jsonPath("$[0].id", is(recipe.getId())));


        verify(service, times(1)).getAll();
    }

    @Test
    void deleteRecipeById() throws Exception {
        RecipeDto recipe = recipeDto();
        mvc.perform(delete("/api/v1/recipes/")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isMethodNotAllowed());

        mvc.perform(delete("/api/v1/recipes/" + recipe.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(service, times(1)).deleteById(recipe.getId());
    }
}