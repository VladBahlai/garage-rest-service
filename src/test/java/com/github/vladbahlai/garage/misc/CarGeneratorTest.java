package com.github.vladbahlai.garage.misc;

import com.github.vladbahlai.garage.model.Brand;
import com.github.vladbahlai.garage.model.Car;
import com.github.vladbahlai.garage.model.Category;
import com.github.vladbahlai.garage.model.Model;
import com.github.vladbahlai.garage.service.CarService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest(classes = CarGenerator.class)
class CarGeneratorTest {

    @MockBean
    CarService carService;
    @Autowired
    CarGenerator carGenerator;

    @Test
    void shouldGenerateCars() {
        List<Model> models = new ArrayList<>(Arrays.asList(
                new Model(1L, "test1", new Brand(1L, "testBrand1")),
                new Model(2L, "test2", new Brand(2L, "testBrand2")),
                new Model(3L, "test3", new Brand(3L, "testBrand3"))));
        List<Category> categories = new ArrayList<>(Arrays.asList(
                new Category(1L, "test1"),
                new Category(2L, "test2"),
                new Category(3L, "test3")));
        carGenerator.generateCarData(models, categories, 10);
        verify(carService, times(10)).saveCar(any(Car.class));
    }
}
