package com.github.vladbahlai.garage.dto;

import javax.validation.constraints.NotBlank;
import java.util.Objects;

public class ModelDTO {

    @NotBlank
    private String name;
    @NotBlank
    private BrandDTO brand;

    public ModelDTO(String name, BrandDTO brand) {
        this.name = name;
        this.brand = brand;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BrandDTO getBrand() {
        return brand;
    }

    public void setBrand(BrandDTO brand) {
        this.brand = brand;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ModelDTO modelDto = (ModelDTO) o;
        return Objects.equals(name, modelDto.name) && Objects.equals(brand, modelDto.brand);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, brand);
    }

    @Override
    public String toString() {
        return "ModelDTO{" +
                "name='" + name + '\'' +
                ", brand=" + brand +
                '}';
    }
}
