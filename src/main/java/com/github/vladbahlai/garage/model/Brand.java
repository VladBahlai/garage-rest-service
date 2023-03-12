package com.github.vladbahlai.garage.model;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "brands")
public class Brand extends BaseEntity {

    @OneToMany(mappedBy = "brand")
    private List<Model> models = new ArrayList<>();

    @OneToMany(mappedBy = "brand")
    private List<Car> cars = new ArrayList<>();

    public Brand() {
    }

    public Brand(Long id, String name) {
        super(id, name);
    }

    public Brand(String name) {
        super(name);
    }

    public List<Model> getModels() {
        return models;
    }

    public void setModels(List<Model> models) {
        this.models = models;
    }

    public List<Car> getCars() {
        return cars;
    }

    public void setCars(List<Car> cars) {
        this.cars = cars;
    }

    @Override
    public String toString() {
        return "Brand{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
