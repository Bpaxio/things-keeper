package ru.bbpax.keeper.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.bbpax.keeper.model.Recipe;
import ru.bbpax.keeper.service.RecipeService;

import java.util.List;

@RestController
@RequestMapping("api/v1/recipe")
@AllArgsConstructor
@Api(value="RecipesRest", description = "Recipe REST API")
public class RecipeController {
    private final RecipeService service;

    @PostMapping
    @ApiOperation(value = "create")
    public Recipe create(@RequestBody Recipe recipeDto) {
        return service.create(recipeDto);
    }

    @PutMapping
    @ApiOperation(value = "update")
    public Recipe update(@RequestBody Recipe recipeDto) {
        return service.update(recipeDto);
    }

    @GetMapping("{id}")
    @ResponseBody
    @ApiOperation(value = "get")
    public Recipe get(@PathVariable String id) {
        return service.getById(id);
    }

    @GetMapping
    @ResponseBody
    // TODO: 2019-05-07 add filters as query params
    @ApiOperation(value = "getAll")
    public List<Recipe> getAll() {
        return service.getAll();
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "deleteById")
    public void deleteById(@PathVariable String id) {
        service.deleteById(id);
    }
}
