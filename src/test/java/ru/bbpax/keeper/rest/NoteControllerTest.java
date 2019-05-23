package ru.bbpax.keeper.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import ru.bbpax.keeper.rest.dto.NoteDto;
import ru.bbpax.keeper.service.NoteService;

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
import static ru.bbpax.keeper.util.EntityUtil.noteDto;

/**
 * @author Vlad Rakhlinskii
 * Created on 08.05.2019.
 */
@ExtendWith(SpringExtension.class)
@WebMvcTest
@WithMockUser
class NoteControllerTest {
    @Configuration
    @Import({ NoteController.class, MockMvcSecurityConfig.class })
    static class Config {
    }

    @Autowired
    private MockMvc mvc;
    @MockBean
    private NoteService service;

    @Test
    void createNote() throws Exception {
        NoteDto note = noteDto();
        mvc.perform(post("/notes/")
                .content(note.getDescription())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        mvc.perform(post("/notes/")
                .content(new ObjectMapper().writeValueAsString(note))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(service, times(1)).create(note);
    }

    @Test
    void updateNote() throws Exception {
        NoteDto note = noteDto();

        mvc.perform(put("/notes/" + note.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isMethodNotAllowed());

        MvcResult mvcResult = mvc.perform(put("/notes/")
                .content(new ObjectMapper().writeValueAsString(note))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
        assertEquals(0, mvcResult.getResponse().getContentLength());

        verify(service, times(1)).update(note);
    }

    @Test
    void getNote() throws Exception {
        NoteDto note = noteDto();
        when(service.getById(note.getId()))
                .thenReturn(note);

        mvc.perform(get("/notes/" + note.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(note.getId())))
                .andExpect(jsonPath("$.description", is(note.getDescription())))
                .andExpect(jsonPath("$.tags", hasSize(3)))
                .andExpect(jsonPath("$.created", is(note.getCreated().toString())))
                .andExpect(jsonPath("$.title", is(note.getTitle())))
                .andExpect(jsonPath("$.id", is(note.getId())));

        verify(service, times(1)).getById(note.getId());
    }

    @Test
    void getNotes() throws Exception {
        NoteDto note = noteDto();
        when(service.getAll(any()))
                .thenReturn(Collections.singletonList(note));

        mvc.perform(get("/notes/")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(note.getId())))
                .andExpect(jsonPath("$[0].description", is(note.getDescription())))
                .andExpect(jsonPath("$[0].tags", hasSize(3)))
                .andExpect(jsonPath("$[0].created", is(note.getCreated().toString())))
                .andExpect(jsonPath("$[0].title", is(note.getTitle())))
                .andExpect(jsonPath("$[0].id", is(note.getId())));


        verify(service, times(1)).getAll(any());
    }

    @Test
    void deleteNoteById() throws Exception {
        NoteDto note = noteDto();
        mvc.perform(delete("/notes/")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isMethodNotAllowed());

        mvc.perform(delete("/notes/" + note.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(service, times(1)).deleteById(note.getId());
    }
}