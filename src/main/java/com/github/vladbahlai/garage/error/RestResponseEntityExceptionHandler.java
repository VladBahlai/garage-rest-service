package com.github.vladbahlai.garage.error;

import com.github.vladbahlai.garage.exception.*;
import com.github.vladbahlai.garage.utils.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@RestControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = CategoryNotFoundException.class)
    protected ResponseEntity<ErrorResponse> handleConflict(CategoryNotFoundException e) {
        ErrorResponse response = new ErrorResponse(e.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = BrandNotFoundException.class)
    protected ResponseEntity<ErrorResponse> handleConflict(BrandNotFoundException e) {
        ErrorResponse response = new ErrorResponse(e.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = CarNotFoundException.class)
    protected ResponseEntity<ErrorResponse> handleConflict(CarNotFoundException e) {
        ErrorResponse response = new ErrorResponse(e.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = ModelNotFoundException.class)
    protected ResponseEntity<ErrorResponse> handleConflict(ModelNotFoundException e) {
        ErrorResponse response = new ErrorResponse(e.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = UniqueNameConstraintException.class)
    protected ResponseEntity<ErrorResponse> handleConflict(UniqueNameConstraintException e) {
        ErrorResponse response = new ErrorResponse(e.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = BrandDoesntHaveModelException.class)
    protected ResponseEntity<ErrorResponse> handleConflict(BrandDoesntHaveModelException e) {
        ErrorResponse response = new ErrorResponse(e.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
