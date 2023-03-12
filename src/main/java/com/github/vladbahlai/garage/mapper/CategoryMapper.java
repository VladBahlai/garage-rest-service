package com.github.vladbahlai.garage.mapper;

import com.github.vladbahlai.garage.dto.CategoryDTO;
import com.github.vladbahlai.garage.model.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public CategoryDTO toCategoryDTO(Category category) {
        return new CategoryDTO(category.getName());
    }

    public Category toCategory(CategoryDTO categoryDTO) {
        return new Category(categoryDTO.getName());
    }
}
