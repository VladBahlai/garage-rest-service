package com.github.vladbahlai.garage.repository;


import com.github.vladbahlai.garage.model.Brand;
import com.github.vladbahlai.garage.model.Car;
import com.github.vladbahlai.garage.model.Model;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class CarRepositoryTest {

    @Autowired
    CarRepository carRepository;
    @Autowired
    BrandRepository brandRepository;
    @Autowired
    ModelRepository modelRepository;

    @Test
    void shouldReadAndWrite() {
        Brand brand = brandRepository.save(new Brand("test"));
        Model model = modelRepository.save(new Model("test", brand));
        Car expected = carRepository.save(new Car(brand, LocalDate.of(2002, 2, 2), model));
        Optional<Car> actual = carRepository.findById(expected.getId());

        assertTrue(actual.isPresent());
        assertEquals(expected, actual.get());

    }

    @Test
    void shouldReturnListCarsWithTheGivenParameters() {
        Brand brand1 = brandRepository.save(new Brand("testBrand1"));
        Brand brand2 = brandRepository.save(new Brand("testBrand2"));
        Model model1 = modelRepository.save(new Model("testModel1", brand1));
        Model model2 = modelRepository.save(new Model("testModel2", brand2));
        Model model3 = modelRepository.save(new Model("testModel3", brand1));
        Model model4 = modelRepository.save(new Model("testModel4", brand2));

        List<Car> expected = new ArrayList<>();
        carRepository.save(new Car(brand1, LocalDate.of(2002, 2, 2), model1));
        carRepository.save(new Car(brand1, LocalDate.of(2004, 2, 2), model3));
        carRepository.save(new Car(brand1, LocalDate.of(2007, 2, 2), model1));

        carRepository.save(new Car(brand2, LocalDate.of(2007, 2, 2), model2));
        expected.add(carRepository.save(new Car(brand2, LocalDate.of(2009, 2, 2), model2)));
        expected.add(carRepository.save(new Car(brand2, LocalDate.of(2012, 2, 2), model4)));
        carRepository.save(new Car(brand2, LocalDate.of(2002, 2, 2), model4));


        Pageable paging = PageRequest.of(0, 10, Sort.by("id"));
        List<Car> actual = carRepository.findCarsByBrandNameAndYearRange(brand2.getName(), LocalDate.of(2008, 2, 2), LocalDate.of(2013, 2, 2), paging);

        assertEquals(expected, actual);
    }
}
