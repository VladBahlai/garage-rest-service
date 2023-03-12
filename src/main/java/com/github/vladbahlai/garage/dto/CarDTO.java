package com.github.vladbahlai.garage.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class CarDTO {

    @NotBlank
    private BrandDTO brand;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate year;
    @NotBlank
    private ModelDTO model;
    private Set<CategoryDTO> categories = new HashSet<>();

    public CarDTO() {
    }

    public CarDTO(BrandDTO brand, LocalDate year, ModelDTO model) {
        this.brand = brand;
        this.year = year;
        this.model = model;
    }

    public CarDTO(BrandDTO brand, LocalDate year, ModelDTO model, Set<CategoryDTO> categories) {
        this.brand = brand;
        this.year = year;
        this.model = model;
        this.categories = categories;
    }

    public BrandDTO getBrand() {
        return brand;
    }

    public void setBrand(BrandDTO brand) {
        this.brand = brand;
    }

    public LocalDate getYear() {
        return year;
    }

    public void setYear(LocalDate year) {
        this.year = year;
    }

    public ModelDTO getModel() {
        return model;
    }

    public void setModel(ModelDTO model) {
        this.model = model;
    }

    public Set<CategoryDTO> getCategories() {
        return categories;
    }

    public void setCategories(Set<CategoryDTO> categories) {
        this.categories = categories;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CarDTO carDTO = (CarDTO) o;
        return Objects.equals(brand, carDTO.brand) && Objects.equals(year, carDTO.year) && Objects.equals(model, carDTO.model) && Objects.equals(categories, carDTO.categories);
    }

    @Override
    public int hashCode() {
        return Objects.hash(brand, year, model, categories);
    }

    @Override
    public String toString() {
        return "CarDTO{" +
                "brand=" + brand +
                ", year=" + year +
                ", model=" + model +
                ", categories=" + categories +
                '}';
    }
}
