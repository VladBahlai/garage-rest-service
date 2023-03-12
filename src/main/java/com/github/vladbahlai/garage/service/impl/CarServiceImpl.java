package com.github.vladbahlai.garage.service.impl;

import com.github.vladbahlai.garage.exception.BrandDoesntHaveModelException;
import com.github.vladbahlai.garage.exception.CarNotFoundException;
import com.github.vladbahlai.garage.model.Car;
import com.github.vladbahlai.garage.repository.CarRepository;
import com.github.vladbahlai.garage.service.CarService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class CarServiceImpl implements CarService {

    private final CarRepository repository;

    public CarServiceImpl(CarRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Car> getCars() {
        return repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Car getCarById(Long id) {
        return repository.findById(id).orElseThrow(() -> new CarNotFoundException(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Car getCarWithCategories(Long id) {
        return repository.findByIdEagerFetch(id).orElseThrow(() -> new CarNotFoundException(id));

    }

    @Override
    @Transactional
    public void deleteCar(Long id) {
        repository.deleteById(id);
    }

    @Override
    @Transactional
    public Car saveCar(Car car) {
        if (!car.getModel().getBrand().equals(car.getBrand())) {
            throw new BrandDoesntHaveModelException(car.getBrand(), car.getModel());
        }
        return repository.save(car);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Car> getCars(Integer pageNumber, Integer pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy));
        return repository.findAllEagerFetch(paging);
    }

    @Override
    public List<Car> getCarsByBrandAndYear(String brandName, LocalDate minYear, LocalDate maxYear, Integer pageNumber, Integer pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy));
        return repository.findCarsByBrandNameAndYearRange(brandName, minYear, maxYear, paging);
    }

}
