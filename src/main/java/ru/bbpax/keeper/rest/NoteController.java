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
import ru.bbpax.keeper.rest.dto.NoteDto;
import ru.bbpax.keeper.rest.request.NoteFilterRequest;
import ru.bbpax.keeper.service.NoteService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("api/v1/notes")
@AllArgsConstructor
@Api(value = "NotesRest", description = "Note REST API")
public class NoteController {
    private final NoteService service;

    @PostMapping
    @ApiOperation("create")
    public NoteDto create(@RequestBody NoteDto noteDto) {
        return service.create(noteDto);
    }

    @PutMapping
    @ApiOperation("update")
    public NoteDto update(@RequestBody NoteDto noteDto) {
        return service.update(noteDto);
    }

    @GetMapping("{id}")
    @ResponseBody
    @ApiOperation("get")
    public NoteDto get(@PathVariable String id) {
        return service.getById(id);
    }

    @GetMapping
    @ResponseBody
    @ApiOperation("getAll")
    public List<NoteDto> getAll(NoteFilterRequest filter) {
        return service.getAll(filter);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation("deleteById")
    public void deleteById(@PathVariable String id) {
        service.deleteById(id);
    }
}
