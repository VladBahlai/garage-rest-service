package com.github.vladbahlai.garage.exception;

public class CarNotFoundException extends RuntimeException {

    public CarNotFoundException(Long id) {
        super("Couldn't find a Car with id: " + id);
    }
}
