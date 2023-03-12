package com.github.vladbahlai.garage.service;

import com.github.vladbahlai.garage.model.Car;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;


public interface CarService {

    @Transactional(readOnly = true)
    List<Car> getCars();

    @Transactional(readOnly = true)
    Car getCarById(Long id);

    @Transactional(readOnly = true)
    Car getCarWithCategories(Long id);

    @Transactional
    void deleteCar(Long id);

    @Transactional
    Car saveCar(Car car);

    @Transactional(readOnly = true)
    List<Car> getCars(Integer pageNumber, Integer pageSize, String sortBy);

    @Transactional(readOnly = true)
    List<Car> getCarsByBrandAndYear(String brandName, LocalDate minYear, LocalDate maxYear, Integer pageNumber, Integer pageSize, String sortBy);
}
