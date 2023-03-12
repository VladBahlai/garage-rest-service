package com.github.vladbahlai.garage.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.vladbahlai.garage.dto.CategoryDTO;
import com.github.vladbahlai.garage.exception.CategoryNotFoundException;
import com.github.vladbahlai.garage.exception.UniqueNameConstraintException;
import com.github.vladbahlai.garage.mapper.CategoryMapper;
import com.github.vladbahlai.garage.model.Category;
import com.github.vladbahlai.garage.security.SecurityConfig;
import com.github.vladbahlai.garage.service.CategoryService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CategoryController.class)
@Import(SecurityConfig.class)
class CategoryControllerTest {

    @MockBean
    CategoryService service;
    @MockBean
    CategoryMapper mapper;
    @Autowired
    MockMvc mockMvc;

    @Test
    void shouldGetAllCategories() throws Exception {
        List<Category> categories = new ArrayList<>(Arrays.asList(
                new Category(1L, "test1"),
                new Category(2L, "test2"),
                new Category(3L, "test3")));
        List<CategoryDTO> categoriesDTO = new ArrayList<>(Arrays.asList(
                new CategoryDTO("test1"),
                new CategoryDTO("test2"),
                new CategoryDTO("test3")));

        when(service.getCategories(0, 10, "id")).thenReturn(categories);
        when(mapper.toCategoryDTO(categories.get(0))).thenReturn(categoriesDTO.get(0));
        when(mapper.toCategoryDTO(categories.get(1))).thenReturn(categoriesDTO.get(1));
        when(mapper.toCategoryDTO(categories.get(2))).thenReturn(categoriesDTO.get(2));

        this.mockMvc.perform(MockMvcRequestBuilders.
                        get("/api/v1/categories")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(3)))
                .andExpect(jsonPath("$[2].name", Matchers.is("test3")));

    }

    @Test
    void shouldGetCategory() throws Exception {
        Category category = new Category(1L, "test1");
        CategoryDTO categoryDTO = new CategoryDTO("test1");

        when(service.getCategoryById(1L)).thenReturn(category);
        when(mapper.toCategoryDTO(category)).thenReturn(categoryDTO);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/categories/{id}", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.name", is("test1")));

    }

    @Test
    void shouldNotGetCategory() throws Exception {
        when(service.getCategoryById(1L)).thenThrow(CategoryNotFoundException.class);
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/categories/{id}", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldDeleteCategory() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/categories/{id}", "1").with(SecurityMockMvcRequestPostProcessors.jwt()))
                .andExpect(status().isOk());
        verify(service, times(1)).deleteCategory(1L);
    }

    @Test
    void shouldCreateCategory() throws Exception {
        Category category = new Category("test1");
        CategoryDTO categoryDTO = new CategoryDTO("test1");

        when(mapper.toCategory(categoryDTO)).thenReturn(category);
        when(service.saveCategory(category)).thenReturn(new Category(1L, "test1"));

        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/categories").with(SecurityMockMvcRequestPostProcessors.jwt())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(categoryDTO.getName())))
                .andExpect(status().isCreated());

    }

    @Test
    void shouldNotCreateCategoryIfNameExist() throws Exception {
        Category category = new Category("test1");
        CategoryDTO categoryDTO = new CategoryDTO("test1");

        when(mapper.toCategory(categoryDTO)).thenReturn(category);
        when(service.saveCategory(category)).thenThrow(UniqueNameConstraintException.class);

        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/categories").with(SecurityMockMvcRequestPostProcessors.jwt())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(categoryDTO.getName())))
                .andExpect(status().isBadRequest());

    }

    @Test
    void shouldUpdateCategory() throws Exception {
        Category oldCategory = new Category(1L, "test1");
        Category newCategory = new Category("test2");
        CategoryDTO categoryDTO = new CategoryDTO("test2");

        when(mapper.toCategory(categoryDTO)).thenReturn(newCategory);
        when(service.getCategoryById(1L)).thenReturn(oldCategory);
        when(service.saveCategory(new Category(oldCategory.getId(), newCategory.getName()))).thenReturn(new Category(1L, "test2"));

        this.mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/categories/{id}", "1").with(SecurityMockMvcRequestPostProcessors.jwt())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(categoryDTO.getName())))
                .andExpect(status().isOk());

    }

    @Test
    void shouldNotUpdateCategoryIfNameExist() throws Exception {
        Category oldCategory = new Category(1L, "test1");
        Category newCategory = new Category("test2");
        CategoryDTO categoryDTO = new CategoryDTO("test2");

        when(mapper.toCategory(categoryDTO)).thenReturn(newCategory);
        when(service.getCategoryById(1L)).thenReturn(oldCategory);
        when(service.saveCategory(new Category(oldCategory.getId(), newCategory.getName()))).thenThrow(UniqueNameConstraintException.class);

        this.mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/categories/{id}", "1").with(SecurityMockMvcRequestPostProcessors.jwt())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(categoryDTO.getName())))
                .andExpect(status().isBadRequest());

    }

}
