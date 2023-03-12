package com.github.vladbahlai.garage.mapper;

import com.github.vladbahlai.garage.dto.BrandDTO;
import com.github.vladbahlai.garage.dto.ModelDTO;
import com.github.vladbahlai.garage.model.Model;
import com.github.vladbahlai.garage.service.BrandService;
import org.springframework.stereotype.Component;

@Component
public class ModelMapper {

    private final BrandService brandService;

    public ModelMapper(BrandService brandService) {
        this.brandService = brandService;
    }

    public ModelDTO toModelDTO(Model model) {
        return new ModelDTO(model.getName(), new BrandDTO(model.getBrand().getName()));

    }

    public Model toModel(ModelDTO modelDto) {
        return new Model(modelDto.getName(), brandService.getBrandByName(modelDto.getBrand().getName()));
    }
}
