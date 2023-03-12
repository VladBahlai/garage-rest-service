package com.github.vladbahlai.garage.exception;

public class ModelNotFoundException extends RuntimeException {

    public ModelNotFoundException(Long id) {
        super("Couldn't find a Model with id: " + id);
    }

    public ModelNotFoundException(String name) {
        super("Couldn't find a Model with name: " + name);
    }
}
