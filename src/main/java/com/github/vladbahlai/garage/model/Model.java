package com.github.vladbahlai.garage.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "models")
public class Model extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "brand_id", referencedColumnName = "id")
    private Brand brand;

    public Model() {
    }

    public Model(Long id, String name) {
        super(id, name);
    }

    public Model(String name) {
        super(name);
    }

    public Model(String name, Brand brand) {
        super(name);
        this.brand = brand;
    }

    public Model(Long id, String name, Brand brand) {
        super(id, name);
        this.brand = brand;
    }

    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Model model = (Model) o;
        return Objects.equals(brand, model.brand);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), brand);
    }

    @Override
    public String toString() {
        return "Model{" +
                "brand=" + brand +
                ", id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
