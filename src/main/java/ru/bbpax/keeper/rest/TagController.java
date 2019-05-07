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
import ru.bbpax.keeper.model.Tag;
import ru.bbpax.keeper.service.TagService;

import java.util.List;

@RestController
@RequestMapping("api/v1/tag")
@AllArgsConstructor
@Api(value="TagsRest", description = "Tag REST API")
public class TagController {
    private final TagService service;

    @PostMapping
    @ApiOperation(value = "create")
    public Tag createTag(@RequestBody Tag tagDto) {
        return service.create(tagDto);
    }

    @PutMapping
    @ApiOperation(value = "update")
    public Tag updateTag(@RequestBody Tag tagDto) {
        return service.update(tagDto);
    }

    @GetMapping("{id}")
    @ResponseBody
    @ApiOperation(value = "get")
    public Tag getTag(@PathVariable String id) {
        return service.getById(id);
    }

    @GetMapping
    @ResponseBody
    @ApiOperation(value = "getAll")
    public List<Tag> getTags() {
        return service.getAll();
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "deleteById")
    public void deleteTagById(@PathVariable String id) {
        service.deleteById(id);
    }
}
