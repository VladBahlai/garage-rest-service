package com.github.vladbahlai.garage.model;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "categories")
public class Category extends BaseEntity {

    @ManyToMany(mappedBy = "categories")
    private List<Car> cars = new ArrayList<>();

    public Category() {
    }

    public Category(Long id, String name) {
        super(id, name);
    }

    public Category(String name) {
        super(name);
    }

    public List<Car> getCars() {
        return cars;
    }

    public void setCars(List<Car> cars) {
        this.cars = cars;
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
