package com.github.vladbahlai.garage.exception;

public class UniqueNameConstraintException extends RuntimeException {
    public UniqueNameConstraintException(String message) {
        super(message);
    }
}
