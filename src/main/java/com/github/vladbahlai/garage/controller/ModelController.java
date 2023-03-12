package com.github.vladbahlai.garage.controller;

import com.github.vladbahlai.garage.dto.ModelDTO;
import com.github.vladbahlai.garage.mapper.ModelMapper;
import com.github.vladbahlai.garage.model.Model;
import com.github.vladbahlai.garage.service.ModelService;
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
@RequestMapping("/api/v1/models")
public class ModelController {

    private final ModelService service;
    private final ModelMapper mapper;

    public ModelController(ModelService service, ModelMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @Operation(summary = "Get list of models")
    @ApiResponse(responseCode = "200", description = "Received models",
            content = {@Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = ModelDTO.class)))})
    @Parameter(name = "pageNumber", description = "Number of page")
    @Parameter(name = "pageSize", description = "Size of page")
    @Parameter(name = "sortBy", description = "Sorting models by field")
    @GetMapping
    public List<ModelDTO> getAllModels(
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "id") String sortBy) {
        return service.getModels(pageNumber, pageSize, sortBy).stream().map(mapper::toModelDTO).collect(Collectors.toList());
    }

    @Operation(summary = "Get a model by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the model",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ModelDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Model not found",
                    content = @Content)})
    @Parameter(name = "id", description = "Id of model")
    @GetMapping("/{id}")
    public ModelDTO getModel(@PathVariable("id") long id) {
        return mapper.toModelDTO(service.getModelById(id));
    }


    @Operation(summary = "Create model", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Model created", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid data",
                    content = @Content)})
    @PostMapping
    public ResponseEntity<HttpStatus> createModel(@RequestBody ModelDTO modelDTO) {
        Model model = mapper.toModel(modelDTO);
        service.saveModel(model);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(summary = "Delete model", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Model deleted", content = @Content)})
    @Parameter(name = "id", description = "Id of model to be deleted")
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteModel(@PathVariable("id") long id) {
        service.deleteModel(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Update model", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Model updated", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid data",
                    content = @Content)})
    @Parameter(name = "id", description = "Id of model to be updated")
    @PatchMapping("/{id}")
    public ResponseEntity<HttpStatus> updateModel(@PathVariable("id") long id, @RequestBody ModelDTO modelDTO) {
        Model newModel = mapper.toModel(modelDTO);
        Model oldModel = service.getModelById(id);

        oldModel.setName(newModel.getName());
        oldModel.setBrand(newModel.getBrand());

        service.saveModel(oldModel);
        return ResponseEntity.ok(HttpStatus.OK);
    }

}
