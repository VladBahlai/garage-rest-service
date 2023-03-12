package com.github.vladbahlai.garage.misc;

import com.github.vladbahlai.garage.model.Car;
import com.github.vladbahlai.garage.model.Category;
import com.github.vladbahlai.garage.model.Model;
import com.github.vladbahlai.garage.service.CarService;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class CarGenerator {

    private final CarService carService;

    public CarGenerator(CarService carService) {
        this.carService = carService;
    }

    public void generateCarData(List<Model> models, List<Category> categories, int count) {
        for (int i = 0; i < count; i++) {
            Model model = models.get(getRandomInt(0, models.size()));
            Car car = new Car(model.getBrand(), getRandomDate(), model);
            for (int j = 0; j < getRandomInt(1, 3); j++) {
                car.getCategories().add(categories.get(getRandomInt(0, categories.size())));

            }
            carService.saveCar(car);
        }
    }


    private LocalDate getRandomDate() {
        return LocalDate.of(getRandomInt(2000, 2023), getRandomInt(1, 13), getRandomInt(1, 25));
    }

    private int getRandomInt(int start, int end) {
        return ThreadLocalRandom.current().nextInt(start, end);
    }

}
