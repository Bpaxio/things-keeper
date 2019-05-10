package ru.bbpax.keeper.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.bson.types.ObjectId;
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
import ru.bbpax.keeper.model.Tag;
import ru.bbpax.keeper.service.TagService;

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

/**
 * @author Vlad Rakhlinskii
 * Created on 08.05.2019.
 */
@ExtendWith(SpringExtension.class)
@WebMvcTest
class TagControllerTest {
    @Configuration
    @Import({ TagController.class })
    static class Config {
    }

    @Autowired
    private MockMvc mvc;
    @MockBean
    private TagService service;

    private Tag tag() {
        return new Tag(new ObjectId().toHexString(),"See Tomorrow");
    }

    @Test
    void createTag() throws Exception {
        Tag tag = tag();
        mvc.perform(post("/api/v1/tags/")
                .content(tag.getValue())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        mvc.perform(post("/api/v1/tags/")
                .content(new ObjectMapper().writeValueAsString(tag))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(service, times(1)).create(tag);
    }

    @Test
    void updateTag() throws Exception {
        Tag tag = tag();

        mvc.perform(put("/api/v1/tags/" + tag.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isMethodNotAllowed());

        MvcResult mvcResult = mvc.perform(put("/api/v1/tags/")
                .content(new ObjectMapper().writeValueAsString(tag))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
        assertEquals(0, mvcResult.getResponse().getContentLength());

        verify(service, times(1)).update(tag);
    }

    @Test
    void getTag() throws Exception {
        Tag tag = tag();
        when(service.getById(tag.getId()))
                .thenReturn(tag);

        mvc.perform(get("/api/v1/tags/" + tag.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(tag.getId())))
                .andExpect(jsonPath("$.value", is(tag.getValue())));

        verify(service, times(1)).getById(tag.getId());
    }

    @Test
    void getTags() throws Exception {
        Tag tag = tag();
        when(service.getAll())
                .thenReturn(Collections.singletonList(tag));

        mvc.perform(get("/api/v1/tags/")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(tag.getId())))
                .andExpect(jsonPath("$[0].value", is(tag.getValue())));


        verify(service, times(1)).getAll();
    }

    @Test
    void deleteTagById() throws Exception {
        Tag tag = tag();
        mvc.perform(delete("/api/v1/tags/")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isMethodNotAllowed());

        mvc.perform(delete("/api/v1/tags/" + tag.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(service, times(1)).deleteById(tag.getId());
    }
}