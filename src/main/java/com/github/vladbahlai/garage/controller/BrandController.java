package com.github.vladbahlai.garage.controller;

import com.github.vladbahlai.garage.dto.BrandDTO;
import com.github.vladbahlai.garage.mapper.BrandMapper;
import com.github.vladbahlai.garage.model.Brand;
import com.github.vladbahlai.garage.service.BrandService;
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

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/brands")
public class BrandController {

    private final BrandService service;
    private final BrandMapper mapper;

    public BrandController(BrandService service, BrandMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @Operation(summary = "Get list of brands")
    @ApiResponse(responseCode = "200", description = "Received brands",
            content = {@Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = BrandDTO.class)))})
    @Parameter(name = "pageNumber", description = "Number of page")
    @Parameter(name = "pageSize", description = "Size of page")
    @Parameter(name = "sortBy", description = "Sorting brands by field")
    @GetMapping
    public List<BrandDTO> getAllBrands(
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "id") String sortBy) {
        return service.getBrands(pageNumber, pageSize, sortBy).stream().map(mapper::toBrandDTO).collect(Collectors.toList());
    }

    @Operation(summary = "Get a brand by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the brand",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = BrandDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Brand not found",
                    content = @Content)})
    @Parameter(name = "id", description = "Id of brand")
    @GetMapping("/{id}")
    public BrandDTO getBrand(@PathVariable("id") long id) {
        return mapper.toBrandDTO(service.getBrandById(id));
    }


    @Operation(summary = "Create brand", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Brand created", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid data",
                    content = @Content)})
    @PostMapping
    public ResponseEntity<HttpStatus> createBrand(@RequestBody BrandDTO brandDTO) {
        Brand brand = mapper.toBrand(brandDTO);
        service.saveBrand(brand);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(summary = "Delete brand", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Brand deleted", content = @Content)})
    @Parameter(name = "id", description = "Id of brand to be deleted")
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteBrand(@PathVariable("id") long id) {
        service.deleteBrand(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Update brand", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Brand updated", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid data",
                    content = @Content)})
    @Parameter(name = "id", description = "Id of brand to be updated")
    @PatchMapping("/{id}")
    public ResponseEntity<HttpStatus> updateBrand(@PathVariable("id") long id, @RequestBody BrandDTO brandDto) {
        Brand newBrand = mapper.toBrand(brandDto);
        Brand oldBrand = service.getBrandById(id);
        oldBrand.setName(newBrand.getName());
        service.saveBrand(oldBrand);
        return ResponseEntity.ok(HttpStatus.OK);
    }

}
