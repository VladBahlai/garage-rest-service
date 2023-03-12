package com.github.vladbahlai.garage.service.impl;

import com.github.vladbahlai.garage.exception.CategoryNotFoundException;
import com.github.vladbahlai.garage.exception.UniqueNameConstraintException;
import com.github.vladbahlai.garage.model.Category;
import com.github.vladbahlai.garage.repository.CategoryRepository;
import com.github.vladbahlai.garage.service.CategoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository repository;

    public CategoryServiceImpl(CategoryRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Category> getCategories() {
        return repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Category getCategoryById(Long id) {
        return repository.findById(id).orElseThrow(() -> new CategoryNotFoundException(id));
    }

    @Override
    public Category getCategoryByName(String name) {
        return repository.findByName(name).orElseThrow(() -> new CategoryNotFoundException(name));
    }

    @Override
    @Transactional
    public void deleteCategory(Long id) {
        repository.deleteById(id);
    }

    @Override
    @Transactional
    public Category saveCategory(Category category) {
        if ((repository.existsByName(category.getName()) && category.getId() == null) ||
                (category.getId() != null && repository.existsByName(category.getName()) &&
                        !category.getName().equals(repository.findById(category.getId()).orElseThrow(() -> new CategoryNotFoundException(category.getId())).getName()))) {
            throw new UniqueNameConstraintException("Category with " + category.getName() + " name already exist.");
        }

        return repository.save(category);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Category> getCategories(Integer pageNumber, Integer pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy));
        Page<Category> page = repository.findAll(paging);

        if (page.hasContent()) {
            return page.getContent();
        }
        return new ArrayList<>();
    }
}
