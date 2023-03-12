package com.github.vladbahlai.garage.mapper;

import com.github.vladbahlai.garage.dto.CategoryDTO;
import com.github.vladbahlai.garage.model.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = CategoryMapper.class)
public class CategoryMapperTest {

    @Autowired
    CategoryMapper mapper;

    @Test
    void shouldConvertToCategoryDto() {
        Category category = new Category(1L, "test");
        CategoryDTO expected = new CategoryDTO("test");
        CategoryDTO actual = mapper.toCategoryDTO(category);
        assertEquals(expected, actual);
    }


    @Test
    void shouldConvertToCategory() {
        CategoryDTO category = new CategoryDTO("test");
        Category expected = new Category("test");
        Category actual = mapper.toCategory(category);
        assertEquals(expected, actual);
    }
}
