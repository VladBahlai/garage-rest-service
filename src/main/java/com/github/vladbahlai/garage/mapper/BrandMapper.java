package com.github.vladbahlai.garage.mapper;

import com.github.vladbahlai.garage.dto.BrandDTO;
import com.github.vladbahlai.garage.model.Brand;
import org.springframework.stereotype.Component;

@Component
public class BrandMapper {

    public BrandDTO toBrandDTO(Brand brand) {
        return new BrandDTO(brand.getName());

    }

    public Brand toBrand(BrandDTO brandDto) {
        return new Brand(brandDto.getName());

    }

}
