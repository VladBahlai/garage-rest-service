package com.github.vladbahlai.garage;

import com.github.vladbahlai.garage.misc.CarGenerator;
import com.github.vladbahlai.garage.service.CarService;
import com.github.vladbahlai.garage.service.CategoryService;
import com.github.vladbahlai.garage.service.ModelService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("!test")
@Component
public class GarageApplicationRunner implements ApplicationRunner {

    private final CarGenerator generator;
    private final CategoryService categoryService;
    private final ModelService modelService;
    private final CarService carService;

    public GarageApplicationRunner(CarGenerator generator, CategoryService categoryService, ModelService modelService, CarService carService) {
        this.generator = generator;
        this.categoryService = categoryService;
        this.modelService = modelService;
        this.carService = carService;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (carService.getCars().isEmpty()) {
            generator.generateCarData(modelService.getModels(), categoryService.getCategories(), 500);
        }
    }
}
