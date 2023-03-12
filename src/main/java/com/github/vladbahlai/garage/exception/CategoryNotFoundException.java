package com.github.vladbahlai.garage.exception;

public class CategoryNotFoundException extends RuntimeException {

    public CategoryNotFoundException(Long id) {
        super("Couldn't find a Category with id: " + id);
    }

    public CategoryNotFoundException(String name) {
        super("Couldn't find a Category with name: " + name);
    }

}
