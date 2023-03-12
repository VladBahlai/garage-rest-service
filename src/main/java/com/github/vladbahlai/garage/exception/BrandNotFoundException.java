package com.github.vladbahlai.garage.exception;

public class BrandNotFoundException extends RuntimeException {

    public BrandNotFoundException(Long id) {
        super("Couldn't find a Brand with id: " + id);
    }

    public BrandNotFoundException(String name) {
        super("Couldn't find a Brand with name: " + name);
    }

}
