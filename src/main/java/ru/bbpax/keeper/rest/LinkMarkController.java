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
import ru.bbpax.keeper.rest.request.LinkMarkFilterRequest;
import ru.bbpax.keeper.service.LinkMarkService;
import ru.bbpax.keeper.rest.dto.LinkMarkDto;

import java.util.List;

@RestController
@RequestMapping("linkmarks")
@AllArgsConstructor
@Api(value="LinkMarksRest", description = "LinkMark REST API")
public class LinkMarkController {
    private final LinkMarkService service;

    @PostMapping
    @ApiOperation(value = "create",
            authorizations = {
                    @Authorization(
                            value = "JWT",
                            scopes = @AuthorizationScope(scope = "global", description = "commonAccess")
                    )
            })
    public LinkMarkDto create(@RequestBody LinkMarkDto dto) {
        return service.create(dto);
    }

    @PutMapping
    @ApiOperation(value = "update",
            authorizations = {
                    @Authorization(
                            value = "JWT",
                            scopes = @AuthorizationScope(scope = "global", description = "commonAccess")
                    )
            })
    public LinkMarkDto update(@RequestBody LinkMarkDto dto) {
        return service.update(dto);
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
    public LinkMarkDto get(@PathVariable String id) {
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
    public List<LinkMarkDto> getAll(LinkMarkFilterRequest request) {
        return service.getAll(request);
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
    public void deleteById(@PathVariable String id) {
        service.deleteById(id);
    }
}
