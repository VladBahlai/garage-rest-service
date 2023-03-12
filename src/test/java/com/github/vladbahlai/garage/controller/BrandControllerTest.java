package com.github.vladbahlai.garage.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.vladbahlai.garage.dto.BrandDTO;
import com.github.vladbahlai.garage.exception.BrandNotFoundException;
import com.github.vladbahlai.garage.exception.UniqueNameConstraintException;
import com.github.vladbahlai.garage.mapper.BrandMapper;
import com.github.vladbahlai.garage.model.Brand;
import com.github.vladbahlai.garage.security.SecurityConfig;
import com.github.vladbahlai.garage.service.BrandService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BrandController.class)
@Import(SecurityConfig.class)
class BrandControllerTest {

    @MockBean
    BrandService service;
    @MockBean
    BrandMapper mapper;
    @Autowired
    MockMvc mockMvc;

    @Test
    void shouldGetAllBrands() throws Exception {
        List<Brand> brands = new ArrayList<>(Arrays.asList(
                new Brand(1L, "test1"),
                new Brand(2L, "test2"),
                new Brand(3L, "test3")));
        List<BrandDTO> brandsDTO = new ArrayList<>(Arrays.asList(
                new BrandDTO("test1"),
                new BrandDTO("test2"),
                new BrandDTO("test3")));

        when(service.getBrands(0, 10, "id")).thenReturn(brands);
        when(mapper.toBrandDTO(brands.get(0))).thenReturn(brandsDTO.get(0));
        when(mapper.toBrandDTO(brands.get(1))).thenReturn(brandsDTO.get(1));
        when(mapper.toBrandDTO(brands.get(2))).thenReturn(brandsDTO.get(2));

        this.mockMvc.perform(MockMvcRequestBuilders.
                        get("/api/v1/brands")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(3)))
                .andExpect(jsonPath("$[2].name", Matchers.is("test3")));

    }

    @Test
    void shouldGetBrand() throws Exception {
        Brand Brand = new Brand(1L, "test1");
        BrandDTO BrandDTO = new BrandDTO("test1");

        when(service.getBrandById(1L)).thenReturn(Brand);
        when(mapper.toBrandDTO(Brand)).thenReturn(BrandDTO);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/brands/{id}", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.name", is("test1")));

    }

    @Test
    void shouldNotGetBrand() throws Exception {
        when(service.getBrandById(1L)).thenThrow(BrandNotFoundException.class);
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/brands/{id}", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void shouldDeleteBrand() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/brands/{id}", "1").with(SecurityMockMvcRequestPostProcessors.jwt()))
                .andExpect(status().isOk());
        verify(service, times(1)).deleteBrand(1L);
    }

    @Test
    void shouldCreateBrand() throws Exception {
        Brand Brand = new Brand("test1");
        BrandDTO BrandDTO = new BrandDTO("test1");

        when(mapper.toBrand(BrandDTO)).thenReturn(Brand);
        when(service.saveBrand(Brand)).thenReturn(new Brand(1L, "test1"));

        this.mockMvc.perform(post("/api/v1/brands").with(SecurityMockMvcRequestPostProcessors.jwt())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(BrandDTO.getName())))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldNotCreateBrandIfNameExist() throws Exception {
        Brand Brand = new Brand("test1");
        BrandDTO BrandDTO = new BrandDTO("test1");

        when(mapper.toBrand(BrandDTO)).thenReturn(Brand);
        when(service.saveBrand(Brand)).thenThrow(UniqueNameConstraintException.class);

        this.mockMvc.perform(post("/api/v1/brands").with(SecurityMockMvcRequestPostProcessors.jwt())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(BrandDTO.getName())))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldUpdateBrand() throws Exception {
        Brand oldBrand = new Brand(1L, "test1");
        Brand newBrand = new Brand("test2");
        BrandDTO BrandDTO = new BrandDTO("test2");

        when(mapper.toBrand(BrandDTO)).thenReturn(newBrand);
        when(service.getBrandById(1L)).thenReturn(oldBrand);
        when(service.saveBrand(new Brand(oldBrand.getId(), newBrand.getName()))).thenReturn(new Brand(1L, "test2"));

        this.mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/brands/{id}", "1").with(SecurityMockMvcRequestPostProcessors.jwt())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(BrandDTO.getName())))
                .andExpect(status().isOk());

    }

    @Test
    void shouldNotUpdateBrandIfNameExist() throws Exception {
        Brand oldBrand = new Brand(1L, "test1");
        Brand newBrand = new Brand("test2");
        BrandDTO BrandDTO = new BrandDTO("test2");

        when(mapper.toBrand(BrandDTO)).thenReturn(newBrand);
        when(service.getBrandById(1L)).thenReturn(oldBrand);
        when(service.saveBrand(new Brand(oldBrand.getId(), newBrand.getName()))).thenThrow(UniqueNameConstraintException.class);

        this.mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/brands/{id}", "1").with(SecurityMockMvcRequestPostProcessors.jwt())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(BrandDTO.getName())))
                .andExpect(status().isBadRequest());

    }

}
