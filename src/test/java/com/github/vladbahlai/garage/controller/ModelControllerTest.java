package com.github.vladbahlai.garage.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.vladbahlai.garage.dto.BrandDTO;
import com.github.vladbahlai.garage.dto.ModelDTO;
import com.github.vladbahlai.garage.exception.BrandNotFoundException;
import com.github.vladbahlai.garage.exception.ModelNotFoundException;
import com.github.vladbahlai.garage.exception.UniqueNameConstraintException;
import com.github.vladbahlai.garage.mapper.ModelMapper;
import com.github.vladbahlai.garage.model.Brand;
import com.github.vladbahlai.garage.model.Model;
import com.github.vladbahlai.garage.security.SecurityConfig;
import com.github.vladbahlai.garage.service.ModelService;
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

import java.util.*;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ModelController.class)
@Import(SecurityConfig.class)
class ModelControllerTest {

    @MockBean
    ModelService service;
    @MockBean
    ModelMapper mapper;
    @Autowired
    MockMvc mockMvc;

    @Test
    void shouldGetAllModels() throws Exception {
        List<Model> models = new ArrayList<>(Arrays.asList(
                new Model(1L, "test1", new Brand(1L, "testBrand1")),
                new Model(2L, "test2", new Brand(2L, "testBrand2")),
                new Model(3L, "test3", new Brand(3L, "testBrand3"))));
        List<ModelDTO> modelsDTO = new ArrayList<>(Arrays.asList(
                new ModelDTO("test1", new BrandDTO("testBrand1")),
                new ModelDTO("test2", new BrandDTO("testBrand2")),
                new ModelDTO("test3", new BrandDTO("testBrand3"))));

        when(service.getModels(0, 10, "id")).thenReturn(models);
        when(mapper.toModelDTO(models.get(0))).thenReturn(modelsDTO.get(0));
        when(mapper.toModelDTO(models.get(1))).thenReturn(modelsDTO.get(1));
        when(mapper.toModelDTO(models.get(2))).thenReturn(modelsDTO.get(2));

        this.mockMvc.perform(MockMvcRequestBuilders.
                        get("/api/v1/models")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(3)))
                .andExpect(jsonPath("$[2].name", Matchers.is("test3")));

    }

    @Test
    void shouldGetModel() throws Exception {
        Model model = new Model(1L, "test1", new Brand(1L, "testBrand1"));
        ModelDTO modelDTO = new ModelDTO("test1", new BrandDTO("testBrand1"));

        when(service.getModelById(1L)).thenReturn(model);
        when(mapper.toModelDTO(model)).thenReturn(modelDTO);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/models/{id}", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.name", is("test1")));

    }

    @Test
    void shouldNotGetModel() throws Exception {
        when(service.getModelById(1L)).thenThrow(ModelNotFoundException.class);
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/models/{id}", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldDeleteModel() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/models/{id}", "1").with(SecurityMockMvcRequestPostProcessors.jwt()))
                .andExpect(status().isOk());
        verify(service, times(1)).deleteModel(1L);
    }

    @Test
    void shouldCreateModel() throws Exception {
        Model model = new Model("test1", new Brand(1L, "testBrand1"));
        ModelDTO modelDTO = new ModelDTO("test1", new BrandDTO("testBrand1"));

        Map<String, String> map = new HashMap<>();
        map.put("name", modelDTO.getName());
        map.put("brand", modelDTO.getBrand().getName());

        when(mapper.toModel(modelDTO)).thenReturn(model);
        when(service.saveModel(model)).thenReturn(new Model(1L, "test1", new Brand(1L, "testBrand1")));

        String jsonResult = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(map);
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/models").with(SecurityMockMvcRequestPostProcessors.jwt())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonResult))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldNotCreateModelIfModelNameExist() throws Exception {
        Model model = new Model(1L, "test1", new Brand(1L, "testBrand1"));
        ModelDTO modelDTO = new ModelDTO("test1", new BrandDTO("testBrand1"));

        Map<String, String> map = new HashMap<>();
        map.put("name", modelDTO.getName());
        map.put("brand", modelDTO.getBrand().getName());

        when(mapper.toModel(modelDTO)).thenReturn(model);
        when(service.saveModel(model)).thenThrow(UniqueNameConstraintException.class);

        String jsonResult = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(map);
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/models").with(SecurityMockMvcRequestPostProcessors.jwt())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonResult))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldNotCreateModelIfBrandNameDoesntExist() throws Exception {
        ModelDTO modelDTO = new ModelDTO("test1", new BrandDTO("testBrand1"));
        Map<String, String> map = new HashMap<>();
        map.put("name", modelDTO.getName());
        map.put("brand", modelDTO.getBrand().getName());

        when(mapper.toModel(modelDTO)).thenThrow(BrandNotFoundException.class);

        String jsonResult = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(map);
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/models").with(SecurityMockMvcRequestPostProcessors.jwt())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonResult))
                .andExpect(status().isNotFound());

    }

    @Test
    void shouldUpdateModel() throws Exception {
        Model oldModel = new Model(1L, "old", new Brand(1L, "testBrand1"));
        Model newModel = new Model("new", new Brand(1L, "testBrand1"));
        ModelDTO modelDTO = new ModelDTO("new", new BrandDTO("testBrand1"));

        Map<String, String> map = new HashMap<>();
        map.put("name", modelDTO.getName());
        map.put("brand", modelDTO.getBrand().getName());

        when(mapper.toModel(modelDTO)).thenReturn(newModel);
        when(service.getModelById(1L)).thenReturn(oldModel);
        when(service.saveModel(new Model(oldModel.getId(), newModel.getName(), (newModel.getBrand())))).thenReturn(new Model(1L, "new", new Brand(1L, "testBrand1")));

        String jsonResult = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(map);
        this.mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/models/{id}", "1").with(SecurityMockMvcRequestPostProcessors.jwt())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonResult))
                .andExpect(status().isOk());

    }

    @Test
    void shouldNotUpdateModelIfModelNameExist() throws Exception {
        Model oldModel = new Model(1L, "old", new Brand(1L, "testBrand1"));
        Model newModel = new Model("new", new Brand(1L, "testBrand1"));
        ModelDTO modelDTO = new ModelDTO("new", new BrandDTO("testBrand1"));

        Map<String, String> map = new HashMap<>();
        map.put("name", modelDTO.getName());
        map.put("brand", modelDTO.getBrand().getName());

        when(mapper.toModel(modelDTO)).thenReturn(newModel);
        when(service.getModelById(1L)).thenReturn(oldModel);
        when(service.saveModel(new Model(oldModel.getId(), newModel.getName(), (newModel.getBrand())))).thenThrow(UniqueNameConstraintException.class);

        String jsonResult = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(map);
        this.mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/models/{id}", "1").with(SecurityMockMvcRequestPostProcessors.jwt())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonResult))
                .andExpect(status().isBadRequest());

    }

    @Test
    void shouldNotUpdateModelIfBrandNameDoesntExist() throws Exception {
        ModelDTO modelDTO = new ModelDTO("test1", new BrandDTO("testBrand1"));
        Map<String, String> map = new HashMap<>();
        map.put("name", modelDTO.getName());
        map.put("brand", modelDTO.getBrand().getName());

        when(mapper.toModel(modelDTO)).thenThrow(BrandNotFoundException.class);

        String jsonResult = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(map);
        this.mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/models/{id}", "1").with(SecurityMockMvcRequestPostProcessors.jwt())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonResult))
                .andExpect(status().isNotFound());

    }

}
