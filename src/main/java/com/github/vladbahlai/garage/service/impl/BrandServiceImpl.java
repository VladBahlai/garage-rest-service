package com.github.vladbahlai.garage.service.impl;

import com.github.vladbahlai.garage.exception.BrandNotFoundException;
import com.github.vladbahlai.garage.exception.UniqueNameConstraintException;
import com.github.vladbahlai.garage.model.Brand;
import com.github.vladbahlai.garage.repository.BrandRepository;
import com.github.vladbahlai.garage.service.BrandService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class BrandServiceImpl implements BrandService {

    private final BrandRepository repository;

    public BrandServiceImpl(BrandRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Brand> getBrands() {
        return repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Brand getBrandById(Long id) {
        return repository.findById(id).orElseThrow(() -> new BrandNotFoundException(id));
    }

    @Override
    public Brand getBrandByName(String name) {
        return repository.findByName(name).orElseThrow(() -> new BrandNotFoundException(name));
    }

    @Override
    @Transactional
    public void deleteBrand(Long id) {
        repository.deleteById(id);
    }

    @Override
    @Transactional
    public Brand saveBrand(Brand brand) {
        if ((brand.getId() == null && repository.existsByName(brand.getName())) ||
                (brand.getId() != null && repository.existsByName(brand.getName()) &&
                        !brand.getName().equals(repository.findById(brand.getId()).orElseThrow(() -> new BrandNotFoundException(brand.getId())).getName()))) {
            throw new UniqueNameConstraintException("Brand with " + brand.getName() + " name already exist.");
        }
        return repository.save(brand);
    }

    @Override
    public List<Brand> getBrands(Integer pageNumber, Integer pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy));
        Page<Brand> page = repository.findAll(paging);

        if (page.hasContent()) {
            return page.getContent();
        }
        return new ArrayList<>();

    }
}
