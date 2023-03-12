package com.github.vladbahlai.garage.mapper;

import com.github.vladbahlai.garage.dto.CarDTO;
import com.github.vladbahlai.garage.dto.CategoryDTO;
import com.github.vladbahlai.garage.dto.ModelDTO;
import com.github.vladbahlai.garage.model.Car;
import com.github.vladbahlai.garage.model.Category;
import com.github.vladbahlai.garage.service.BrandService;
import com.github.vladbahlai.garage.service.CategoryService;
import com.github.vladbahlai.garage.service.ModelService;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class CarMapper {

    private final ModelMapper modelMapper;
    private final CategoryMapper categoryMapper;
    private final ModelService modelService;
    private final CategoryService categoryService;
    private final BrandService brandService;

    public CarMapper(ModelMapper modelMapper, CategoryMapper categoryMapper, ModelService modelService, CategoryService categoryService, BrandService brandService) {
        this.modelMapper = modelMapper;
        this.categoryMapper = categoryMapper;
        this.modelService = modelService;
        this.categoryService = categoryService;
        this.brandService = brandService;
    }

    public CarDTO toCarDto(Car car) {
        ModelDTO model = modelMapper.toModelDTO(car.getModel());
        Set<CategoryDTO> categories = new HashSet<>();
        if (!car.getCategories().isEmpty()) {
            for (Category category : car.getCategories()) {
                categories.add(categoryMapper.toCategoryDTO(category));
            }
        }
        return new CarDTO(model.getBrand(), car.getYear(), model, categories);
    }

    public Car toCar(CarDTO carDto) {
        Car car = new Car(brandService.getBrandByName(carDto.getBrand().getName()), carDto.getYear(), modelService.getModelByName(carDto.getModel().getName()));
        if (!carDto.getCategories().isEmpty()) {
            for (CategoryDTO categoryDTO : carDto.getCategories()) {
                car.getCategories().add(categoryService.getCategoryByName(categoryDTO.getName()));
            }
        }
        return car;
    }
}
