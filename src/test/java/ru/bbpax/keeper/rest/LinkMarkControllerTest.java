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
import ru.bbpax.keeper.service.LinkMarkService;
import ru.bbpax.keeper.rest.dto.LinkMarkDto;

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
import static ru.bbpax.keeper.util.EntityUtil.linkMarkDto;

/**
 * @author Vlad Rakhlinskii
 * Created on 08.05.2019.
 */
@ExtendWith(SpringExtension.class)
@WebMvcTest
@WithMockUser
class LinkMarkControllerTest {
    @Configuration
    @Import({ LinkMarkController.class, MockMvcSecurityConfig.class })
    static class Config {
    }

    @Autowired
    private MockMvc mvc;
    @MockBean
    private LinkMarkService service;

    @Test
    void createLinkMark() throws Exception {
        LinkMarkDto linkMark = linkMarkDto();
        mvc.perform(post("/api/v1/linkmarks/")
                .content(linkMark.getDescription())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        mvc.perform(post("/api/v1/linkmarks/")
                .content(new ObjectMapper().writeValueAsString(linkMark))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(service, times(1)).create(linkMark);
    }

    @Test
    void updateLinkMark() throws Exception {
        LinkMarkDto linkMark = linkMarkDto();

        mvc.perform(put("/api/v1/linkmarks/" + linkMark.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isMethodNotAllowed());

        MvcResult mvcResult = mvc.perform(put("/api/v1/linkmarks/")
                .content(new ObjectMapper().writeValueAsString(linkMark))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
        assertEquals(0, mvcResult.getResponse().getContentLength());

        verify(service, times(1)).update(linkMark);
    }

    @Test
    void getLinkMark() throws Exception {
        LinkMarkDto linkMark = linkMarkDto();
        when(service.getById(linkMark.getId()))
                .thenReturn(linkMark);

        mvc.perform(get("/api/v1/linkmarks/" + linkMark.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(linkMark.getId())))
                .andExpect(jsonPath("$.description", is(linkMark.getDescription())))
                .andExpect(jsonPath("$.tags", hasSize(3)))
                .andExpect(jsonPath("$.created", is(linkMark.getCreated().toString())))
                .andExpect(jsonPath("$.title", is(linkMark.getTitle())))
                .andExpect(jsonPath("$.link", is(linkMark.getLink())))
                .andExpect(jsonPath("$.id", is(linkMark.getId())));

        verify(service, times(1)).getById(linkMark.getId());
    }

    @Test
    void getLinkMarks() throws Exception {
        LinkMarkDto linkMark = linkMarkDto();
        when(service.getAll(any()))
                .thenReturn(Collections.singletonList(linkMark));

        mvc.perform(get("/api/v1/linkmarks/")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(linkMark.getId())))
                .andExpect(jsonPath("$[0].description", is(linkMark.getDescription())))
                .andExpect(jsonPath("$[0].tags", hasSize(3)))
                .andExpect(jsonPath("$[0].created", is(linkMark.getCreated().toString())))
                .andExpect(jsonPath("$[0].title", is(linkMark.getTitle())))
                .andExpect(jsonPath("$[0].link", is(linkMark.getLink())))
                .andExpect(jsonPath("$[0].id", is(linkMark.getId())));


        verify(service, times(1)).getAll(any());
    }

    @Test
    void deleteLinkMarkById() throws Exception {
        LinkMarkDto linkMark = linkMarkDto();
        mvc.perform(delete("/api/v1/linkmarks/")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isMethodNotAllowed());

        mvc.perform(delete("/api/v1/linkmarks/" + linkMark.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(service, times(1)).deleteById(linkMark.getId());
    }
}