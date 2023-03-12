package com.github.vladbahlai.garage.controller;

import com.github.vladbahlai.garage.dto.CarDTO;
import com.github.vladbahlai.garage.mapper.CarMapper;
import com.github.vladbahlai.garage.model.Car;
import com.github.vladbahlai.garage.service.CarService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Year;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/cars")
public class CarController {

    private final CarService service;
    private final CarMapper mapper;

    public CarController(CarService service, CarMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @Operation(summary = "Get a car by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the car",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = CarDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Car not found",
                    content = @Content)})
    @Parameter(name = "id", description = "Id of car")
    @GetMapping("/{id}")
    public CarDTO getCar(@PathVariable("id") long id) {
        return mapper.toCarDto(service.getCarWithCategories(id));
    }

    @Operation(summary = "Delete car", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Car deleted", content = @Content)})
    @Parameter(name = "id", description = "Id of car to be deleted")
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteCar(@PathVariable("id") long id) {
        service.deleteCar(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Create car", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Car created", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid data",
                    content = @Content)})
    @PostMapping
    public ResponseEntity<HttpStatus> createCar(@RequestBody CarDTO carDTO) {
        Car car = mapper.toCar(carDTO);
        service.saveCar(car);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(summary = "Update car", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Car updated", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid data",
                    content = @Content)})
    @Parameter(name = "id", description = "Id of car to be updated")
    @PatchMapping("/{id}")
    public ResponseEntity<HttpStatus> updateCar(@PathVariable("id") long id, @RequestBody CarDTO carDTO) {
        Car newCar = mapper.toCar(carDTO);
        Car oldCar = service.getCarWithCategories(id);

        oldCar.setBrand(newCar.getBrand());
        oldCar.setYear(newCar.getYear());
        oldCar.setModel(newCar.getModel());
        oldCar.setCategories(newCar.getCategories());
        service.saveCar(oldCar);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @Operation(summary = "Get list of cars")
    @ApiResponse(responseCode = "200", description = "Received cars",
            content = {@Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = CarDTO.class)))})
    @Parameter(name = "pageNumber", description = "Number of page")
    @Parameter(name = "pageSize", description = "Size of page")
    @Parameter(name = "sortBy", description = "Sorting cars by field")
    @Parameter(name = "brandName", description = "Name of brand car for search")
    @Parameter(name = "minYear", description = "Minimal year of car for search")
    @Parameter(name = "maxYear", description = "Maximal year of car for search")
    @GetMapping()
    public List<CarDTO> getAllCars(
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam Optional<String> brandName,
            @RequestParam Optional<String> minYear,
            @RequestParam Optional<String> maxYear) {
        if (brandName.isPresent() && minYear.isPresent() && maxYear.isPresent()) {
            Year min = Year.parse(minYear.orElseThrow(() -> new RuntimeException("Date can't be a null")));
            Year max = Year.parse(maxYear.orElseThrow(() -> new RuntimeException("Date can't be a null")));

            String name = brandName.orElseThrow(() -> new RuntimeException("Name can't be a null"));
            return service.getCarsByBrandAndYear(name, min.atDay(1), max.atDay(1), pageNumber, pageSize, sortBy).stream().map(mapper::toCarDto).collect(Collectors.toList());
        }
        return service.getCars(pageNumber, pageSize, sortBy).stream().map(mapper::toCarDto).collect(Collectors.toList());
    }
}
