package ru.bbpax.keeper.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import io.swagger.annotations.AuthorizationScope;
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
@RequestMapping("tags")
@AllArgsConstructor
@Api(value="TagsRest", description = "Tag REST API")
public class TagController {
    private final TagService service;

    @PostMapping
    @ApiOperation(value = "create",
            authorizations = {
                    @Authorization(
                            value = "JWT",
                            scopes = @AuthorizationScope(scope = "global", description = "commonAccess")
                    )
            })
    public Tag createTag(@RequestBody Tag tag) {
        return service.create(tag);
    }

    @PutMapping
    @ApiOperation(value = "update",
            authorizations = {
                    @Authorization(
                            value = "JWT",
                            scopes = @AuthorizationScope(scope = "global", description = "commonAccess")
                    )
            })
    public Tag updateTag(@RequestBody Tag tag) {
        return service.update(tag);
    }

    @GetMapping("{id}")
    @ResponseBody
    @ApiOperation(value = "get",
            authorizations = {
                    @Authorization(
                            value = "JWT",
                            scopes = @AuthorizationScope(scope = "global", description = "commonAccess")
                    )
            })
    public Tag getTag(@PathVariable String id) {
        return service.getById(id);
    }

    @GetMapping
    @ResponseBody
    @ApiOperation(value = "getAll",
            authorizations = {
                    @Authorization(
                            value = "JWT",
                            scopes = @AuthorizationScope(scope = "global", description = "commonAccess")
                    )
            })
    public List<Tag> getTags() {
        return service.getAll();
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "deleteById",
            authorizations = {
                    @Authorization(
                            value = "JWT",
                            scopes = @AuthorizationScope(scope = "global", description = "commonAccess")
                    )
            })
    public void deleteTagById(@PathVariable String id) {
        service.deleteById(id);
    }
}
