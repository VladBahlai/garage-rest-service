package com.github.vladbahlai.garage.dto;

import javax.validation.constraints.NotBlank;
import java.util.Objects;

public class BrandDTO {

    @NotBlank
    private String name;

    public BrandDTO(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BrandDTO brandDto = (BrandDTO) o;
        return Objects.equals(name, brandDto.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "BrandDTO{" +
                "name='" + name + '\'' +
                '}';
    }
}
