package com.github.vladbahlai.garage.service;

import com.github.vladbahlai.garage.exception.BrandDoesntHaveModelException;
import com.github.vladbahlai.garage.model.Brand;
import com.github.vladbahlai.garage.model.Car;
import com.github.vladbahlai.garage.model.Model;
import com.github.vladbahlai.garage.repository.CarRepository;
import com.github.vladbahlai.garage.service.impl.CarServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = CarServiceImpl.class)
public class CarServiceTest {

    @MockBean
    CarRepository repository;
    @Autowired
    CarService service;

    @Test
    void shouldCreateCar() {
        Brand brand = new Brand(1L, "test");
        Car toSave = new Car(brand, LocalDate.of(2002, 2, 2), new Model(1L, "test", brand));
        Car expected = new Car(1L, brand, LocalDate.of(2002, 2, 2), new Model(1L, "test", brand));

        when(repository.save(toSave)).thenReturn(new Car(1L, brand, LocalDate.of(2002, 2, 2), new Model(1L, "test", brand)));
        Car actual = service.saveCar(toSave);
        assertEquals(expected, actual);
    }

    @Test
    void shouldUpdateCar() {
        Brand brand = new Brand(1L, "test");
        Car toSave = new Car(1L, brand, LocalDate.of(2002, 2, 2), new Model(1L, "test1", brand));
        Car expected = new Car(1L, brand, LocalDate.of(2002, 2, 2), new Model(1L, "test1", brand));

        when(repository.save(toSave)).thenReturn(new Car(1L, brand, LocalDate.of(2002, 2, 2), new Model(1L, "test1", brand)));
        Car actual = service.saveCar(toSave);
        assertEquals(expected, actual);
    }

    @Test
    void shouldThrowExceptionIfModelNotBelongBrand() {
        Brand brand = new Brand(1L, "test");
        Car toSave = new Car(brand, LocalDate.of(2002, 2, 2), new Model(1L, "test", new Brand(2L, "test2")));

        Exception actualException = assertThrows(BrandDoesntHaveModelException.class, () -> service.saveCar(toSave));

        String expectedExceptionMessage = "Brand test doesn't have model with name test";
        assertEquals(expectedExceptionMessage, actualException.getMessage());
    }
}
