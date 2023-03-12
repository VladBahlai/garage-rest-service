package com.github.vladbahlai.garage.exception;

import com.github.vladbahlai.garage.model.Brand;
import com.github.vladbahlai.garage.model.Model;

public class BrandDoesntHaveModelException extends RuntimeException {

    public BrandDoesntHaveModelException(Brand brand, Model model) {
        super("Brand " + brand.getName() + " doesn't have model with name " + model.getName());
    }
}
