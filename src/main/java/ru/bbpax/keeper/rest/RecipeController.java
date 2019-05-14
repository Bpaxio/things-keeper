package ru.bbpax.keeper.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import ru.bbpax.keeper.rest.request.RecipeFilterRequest;
import ru.bbpax.keeper.service.RecipeService;
import ru.bbpax.keeper.rest.dto.RecipeDto;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("api/v1/recipes")
@AllArgsConstructor
@Api(value="RecipesRest", description = "Recipe REST API")
public class RecipeController {
    private final RecipeService service;

    @PostMapping
    @ApiOperation("create")
    public RecipeDto create(@RequestBody RecipeDto dto) {
        log.info("create request: {}", dto);
        return service.create(dto);
    }

    @PutMapping
    @ApiOperation("update")
    public RecipeDto update(@RequestBody RecipeDto dto) {
        return service.update(dto);
    }

    @GetMapping("{id}")
    @ResponseBody
    @ApiOperation("get")
    public RecipeDto get(@PathVariable String id) {
        return service.getById(id);
    }

    @GetMapping
    @ResponseBody
    @ApiOperation("getAll")
    public List<RecipeDto> getAll(RecipeFilterRequest request) {
        return service.getAll(request);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation("deleteById")
    public void deleteById(@PathVariable String id) {
        service.deleteById(id);
    }
}
