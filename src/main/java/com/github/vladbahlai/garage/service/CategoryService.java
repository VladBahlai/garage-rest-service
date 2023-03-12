package com.github.vladbahlai.garage.service;

import com.github.vladbahlai.garage.model.Category;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CategoryService {

    @Transactional(readOnly = true)
    List<Category> getCategories();

    @Transactional(readOnly = true)
    Category getCategoryById(Long id);

    @Transactional(readOnly = true)
    Category getCategoryByName(String name);

    @Transactional
    void deleteCategory(Long id);

    @Transactional
    Category saveCategory(Category category);

    @Transactional(readOnly = true)
    List<Category> getCategories(Integer pageNumber, Integer pageSize, String sortBy);

}
