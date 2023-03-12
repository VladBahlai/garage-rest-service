package com.github.vladbahlai.garage.controller;

import com.github.vladbahlai.garage.dto.CategoryDTO;
import com.github.vladbahlai.garage.mapper.CategoryMapper;
import com.github.vladbahlai.garage.model.Category;
import com.github.vladbahlai.garage.service.CategoryService;
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
@RequestMapping("/api/v1/categories")
public class CategoryController {

    private final CategoryService service;
    private final CategoryMapper mapper;


    public CategoryController(CategoryService service, CategoryMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @Operation(summary = "Get list of categories")
    @ApiResponse(responseCode = "200", description = "Received categories",
            content = {@Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = CategoryDTO.class)))})
    @Parameter(name = "pageNumber", description = "Number of page")
    @Parameter(name = "pageSize", description = "Size of page")
    @Parameter(name = "sortBy", description = "Sorting categories by field")
    @GetMapping
    public List<CategoryDTO> getAllCategories(
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "id") String sortBy) {
        return service.getCategories(pageNumber, pageSize, sortBy).stream().map(mapper::toCategoryDTO).collect(Collectors.toList());
    }

    @Operation(summary = "Get a category by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the category",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = CategoryDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Category not found",
                    content = @Content)})
    @Parameter(name = "id", description = "Id of category")
    @GetMapping("/{id}")
    public CategoryDTO getCategory(@PathVariable("id") long id) {
        return mapper.toCategoryDTO(service.getCategoryById(id));
    }

    @Operation(summary = "Create category", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Category created", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid data",
                    content = @Content)})
    @PostMapping
    public ResponseEntity<HttpStatus> createCategory(@RequestBody CategoryDTO categoryDTO) {
        Category category = mapper.toCategory(categoryDTO);
        service.saveCategory(category);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(summary = "Delete category", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category deleted", content = @Content)})
    @Parameter(name = "id", description = "Id of category to be deleted")
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteCategory(@PathVariable("id") long id) {
        service.deleteCategory(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Update category", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category updated", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid data",
                    content = @Content)})
    @Parameter(name = "id", description = "Id of category to be updated")
    @PatchMapping("/{id}")
    public ResponseEntity<HttpStatus> updateCategory(@PathVariable("id") long id, @RequestBody CategoryDTO categoryDto) {
        Category newCategory = mapper.toCategory(categoryDto);
        Category oldCategory = service.getCategoryById(id);
        oldCategory.setName(newCategory.getName());
        service.saveCategory(oldCategory);

        return ResponseEntity.ok(HttpStatus.OK);
    }

}
