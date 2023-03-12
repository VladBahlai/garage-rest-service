package com.github.vladbahlai.garage.service;

import com.github.vladbahlai.garage.model.Brand;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface BrandService {

    @Transactional(readOnly = true)
    List<Brand> getBrands();

    @Transactional(readOnly = true)
    Brand getBrandById(Long id);

    @Transactional(readOnly = true)
    Brand getBrandByName(String name);

    @Transactional
    void deleteBrand(Long id);

    @Transactional
    Brand saveBrand(Brand brand);

    @Transactional(readOnly = true)
    List<Brand> getBrands(Integer pageNumber, Integer pageSize, String sortBy);
}
