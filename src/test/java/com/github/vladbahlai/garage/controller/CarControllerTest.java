package com.github.vladbahlai.garage.controller;


import com.github.vladbahlai.garage.dto.BrandDTO;
import com.github.vladbahlai.garage.dto.CarDTO;
import com.github.vladbahlai.garage.dto.CategoryDTO;
import com.github.vladbahlai.garage.dto.ModelDTO;
import com.github.vladbahlai.garage.exception.BrandDoesntHaveModelException;
import com.github.vladbahlai.garage.exception.CarNotFoundException;
import com.github.vladbahlai.garage.mapper.CarMapper;
import com.github.vladbahlai.garage.model.Brand;
import com.github.vladbahlai.garage.model.Car;
import com.github.vladbahlai.garage.model.Category;
import com.github.vladbahlai.garage.model.Model;
import com.github.vladbahlai.garage.security.SecurityConfig;
import com.github.vladbahlai.garage.service.CarService;
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

import java.time.LocalDate;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CarController.class)
@Import(SecurityConfig.class)
class CarControllerTest {

    private static final String EOL = System.lineSeparator();

    @MockBean
    CarService service;
    @MockBean
    CarMapper mapper;
    @Autowired
    MockMvc mockMvc;

    @Test
    void shouldGetAllCars() throws Exception {
        Model model = new Model(1L, "testModel", new Brand(1L, "testBrand"));
        ModelDTO modelDTO = new ModelDTO("testModel", new BrandDTO("testBrand"));
        List<Car> cars = Arrays.asList(
                new Car(1L, model.getBrand(), LocalDate.of(2002, 2, 2), model),
                new Car(2L, model.getBrand(), LocalDate.of(2004, 2, 2), model));
        List<CarDTO> carsDTO = Arrays.asList(
                new CarDTO(modelDTO.getBrand(), LocalDate.of(2002, 2, 2), modelDTO),
                new CarDTO(modelDTO.getBrand(), LocalDate.of(2004, 2, 2), modelDTO));

        when(service.getCars(0, 10, "id")).thenReturn(cars);
        when(mapper.toCarDto(cars.get(0))).thenReturn(carsDTO.get(0));
        when(mapper.toCarDto(cars.get(1))).thenReturn(carsDTO.get(1));

        this.mockMvc.perform(MockMvcRequestBuilders.
                        get("/api/v1/cars")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[1].brand.name", Matchers.is("testBrand")));

    }

    @Test
    void shouldGetCarsWithParameters() throws Exception {
        Model model = new Model(1L, "testModel", new Brand(1L, "testBrand"));
        ModelDTO modelDTO = new ModelDTO("testModel", new BrandDTO("testBrand"));
        List<Car> cars = Arrays.asList(
                new Car(1L, model.getBrand(), LocalDate.of(2002, 2, 2), model),
                new Car(2L, model.getBrand(), LocalDate.of(2004, 2, 2), model));
        List<CarDTO> carsDTO = Arrays.asList(
                new CarDTO(modelDTO.getBrand(), LocalDate.of(2002, 2, 2), modelDTO),
                new CarDTO(modelDTO.getBrand(), LocalDate.of(2004, 2, 2), modelDTO));

        String minYear = "2000";
        String maxYear = "2005";

        when(service.getCarsByBrandAndYear("testBrand", Year.parse(minYear).atDay(1), Year.parse(maxYear).atDay(1), 0, 10, "id")).thenReturn(cars);
        when(mapper.toCarDto(cars.get(0))).thenReturn(carsDTO.get(0));
        when(mapper.toCarDto(cars.get(1))).thenReturn(carsDTO.get(1));

        this.mockMvc.perform(MockMvcRequestBuilders.
                        get("/api/v1/cars")
                        .param("brandName", "testBrand")
                        .param("minYear", "2000")
                        .param("maxYear", "2005")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[1].brand.name", Matchers.is("testBrand")));

    }

    @Test
    void shouldGetCar() throws Exception {
        Model model = new Model(1L, "testModel", new Brand(1L, "testBrand"));
        ModelDTO modelDTO = new ModelDTO("testModel", new BrandDTO("testBrand"));
        Car car = new Car(1L, model.getBrand(), LocalDate.of(2002, 2, 2), model);
        car.getCategories().add(new Category(1L, "testCategory"));
        CarDTO carDTO = new CarDTO(modelDTO.getBrand(), LocalDate.of(2002, 2, 2), modelDTO, new HashSet<>(Arrays.asList(new CategoryDTO("testCategory"))));


        when(service.getCarWithCategories(1L)).thenReturn(car);
        when(mapper.toCarDto(car)).thenReturn(carDTO);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/cars/{id}", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.brand.name", Matchers.is(carDTO.getBrand().getName())))
                .andExpect(jsonPath("$.year", Matchers.is(carDTO.getYear().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))))
                .andExpect(jsonPath("$.categories[0].name", Matchers.is("testCategory")));
    }

    @Test
    void shouldNotGetCar() throws Exception {
        when(service.getCarWithCategories(1L)).thenThrow(CarNotFoundException.class);
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/cars/{id}", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldDeleteCar() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/cars/{id}", "1").with(SecurityMockMvcRequestPostProcessors.jwt()))
                .andExpect(status().isOk());
        verify(service, times(1)).deleteCar(1L);
    }

    @Test
    void shouldCreateCar() throws Exception {
        Model model = new Model(1L, "testModel", new Brand(1L, "testBrand"));
        Car car = new Car(model.getBrand(), LocalDate.of(2002, 2, 2), model);
        car.getCategories().add(new Category(1L, "testCategory"));

        ModelDTO modelDTO = new ModelDTO("testModel", new BrandDTO("testBrand"));
        CarDTO carDTO = new CarDTO(modelDTO.getBrand(), LocalDate.of(2002, 2, 2), modelDTO, new HashSet<>(Arrays.asList(new CategoryDTO("testCategory"))));

        when(mapper.toCar(carDTO)).thenReturn(car);
        when(service.saveCar(car)).thenReturn(new Car(1L, model.getBrand(), LocalDate.of(2002, 2, 2), model));

        String json = "{" + EOL +
                "        \"brand\": \"testBrand\"," + EOL +
                "        \"year\": \"2002-02-02\"," + EOL +
                "        \"model\": {" + EOL +
                "            \"name\": \"testModel\"," + EOL +
                "            \"brand\":  \"testBrand\"" + EOL +
                "        }," + EOL +
                "        \"categories\":[" + EOL +
                "            \"testCategory\"" + EOL +
                "        ]" + EOL +
                "}";
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/cars").with(SecurityMockMvcRequestPostProcessors.jwt())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());

    }

    @Test
    void shouldThrowExceptionIfCarModelBrandDoesntMatchWithBrandCarWhenCreateCar() throws Exception {
        Model model = new Model(1L, "testModel", new Brand(1L, "testBrand"));
        Car car = new Car(new Brand(2L, "testBrand1"), LocalDate.of(2002, 2, 2), model);
        car.getCategories().add(new Category(1L, "testCategory"));

        ModelDTO modelDTO = new ModelDTO("testModel", new BrandDTO("testBrand"));
        CarDTO carDTO = new CarDTO(new BrandDTO("testBrand1"), LocalDate.of(2002, 2, 2), modelDTO, new HashSet<>(Arrays.asList(new CategoryDTO("testCategory"))));

        when(mapper.toCar(carDTO)).thenReturn(car);
        when(service.saveCar(car)).thenThrow(BrandDoesntHaveModelException.class);
        String json = "{" + EOL +
                "        \"brand\": \"testBrand1\"," + EOL +
                "        \"year\": \"2002-02-02\"," + EOL +
                "        \"model\": {" + EOL +
                "            \"name\": \"testModel\"," + EOL +
                "            \"brand\":  \"testBrand\"" + EOL +
                "        }," + EOL +
                "        \"categories\":[" + EOL +
                "            \"testCategory\"" + EOL +
                "        ]" + EOL +
                "}";
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/cars").with(SecurityMockMvcRequestPostProcessors.jwt())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());

    }

    @Test
    void shouldUpdateCar() throws Exception {
        Model model = new Model(1L, "testModel", new Brand(1L, "testBrand"));
        Car oldCar = new Car(1L, model.getBrand(), LocalDate.of(2004, 2, 2), model);
        Car newCar = new Car(model.getBrand(), LocalDate.of(2002, 2, 2), model);

        newCar.getCategories().add(new Category(1L, "testCategory"));
        Car toSave = new Car(oldCar.getId(), newCar.getBrand(), newCar.getYear(), newCar.getModel());
        toSave.setCategories(newCar.getCategories());

        ModelDTO modelDTO = new ModelDTO("testModel", new BrandDTO("testBrand"));
        CarDTO carDTO = new CarDTO(modelDTO.getBrand(), LocalDate.of(2002, 4, 2), modelDTO, new HashSet<>(Arrays.asList(new CategoryDTO("testCategory"))));

        when(mapper.toCar(carDTO)).thenReturn(newCar);
        when(service.getCarWithCategories(1L)).thenReturn(oldCar);
        when(service.saveCar(toSave)).thenReturn(new Car(1L, model.getBrand(), LocalDate.of(2004, 2, 2), model));

        String json = "{" + EOL +
                "        \"brand\": \"testBrand\"," + EOL +
                "        \"year\": \"2002-04-02\"," + EOL +
                "        \"model\": {" + EOL +
                "            \"name\": \"testModel\"," + EOL +
                "            \"brand\":  \"testBrand\"" + EOL +
                "        }," + EOL +
                "        \"categories\":[" + EOL +
                "            \"testCategory\"" + EOL +
                "        ]" + EOL +
                "}";
        this.mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/cars/{id}", "1").with(SecurityMockMvcRequestPostProcessors.jwt())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());

    }

    @Test
    void shouldThrowExceptionIfCarModelBrandDoesntMatchWithBrandCarWhenUpdateCar() throws Exception {

        Model model = new Model(1L, "testModel", new Brand(1L, "testBrand"));
        Car oldCar = new Car(1L, model.getBrand(), LocalDate.of(2004, 2, 2), model);
        Car newCar = new Car(new Brand(2L, "testBrand1"), LocalDate.of(2002, 2, 2), model);

        newCar.getCategories().add(new Category(1L, "testCategory"));
        Car toSave = new Car(oldCar.getId(), newCar.getBrand(), newCar.getYear(), newCar.getModel());
        toSave.setCategories(newCar.getCategories());

        ModelDTO modelDTO = new ModelDTO("testModel", new BrandDTO("testBrand"));
        CarDTO carDTO = new CarDTO(new BrandDTO("testBrand1"), LocalDate.of(2002, 4, 2), modelDTO, new HashSet<>(Arrays.asList(new CategoryDTO("testCategory"))));

        when(mapper.toCar(carDTO)).thenReturn(newCar);
        when(service.getCarWithCategories(1L)).thenReturn(oldCar);
        when(service.saveCar(toSave)).thenThrow(BrandDoesntHaveModelException.class);

        String json = "{" + EOL +
                "        \"brand\": \"testBrand1\"," + EOL +
                "        \"year\": \"2002-04-02\"," + EOL +
                "        \"model\": {" + EOL +
                "            \"name\": \"testModel\"," + EOL +
                "            \"brand\":  \"testBrand\"" + EOL +
                "        }," + EOL +
                "        \"categories\":[" + EOL +
                "            \"testCategory\"" + EOL +
                "        ]" + EOL +
                "}";
        this.mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/cars/{id}", "1").with(SecurityMockMvcRequestPostProcessors.jwt())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }


}
