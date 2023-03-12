package com.github.vladbahlai.garage.service;

import com.github.vladbahlai.garage.exception.UniqueNameConstraintException;
import com.github.vladbahlai.garage.model.Category;
import com.github.vladbahlai.garage.repository.CategoryRepository;
import com.github.vladbahlai.garage.service.impl.CategoryServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = CategoryServiceImpl.class)
public class CategoryServiceTest {

    @MockBean
    CategoryRepository repository;
    @Autowired
    CategoryService service;

    @Test
    void shouldCreateCategory() {
        Category toSave = new Category("test");
        Category expected = new Category(1L, "test");

        when(repository.existsByName(toSave.getName())).thenReturn(false);
        when(repository.save(toSave)).thenReturn(new Category(1L, "test"));

        Category actual = service.saveCategory(toSave);
        assertEquals(expected, actual);

    }

    @Test
    void shouldUpdateCategory() {
        Category expected = new Category(1L, "test");
        Category oldCategory = new Category(1L, "test2");
        Category toSave = new Category(1L, "test");

        when(repository.existsByName(toSave.getName())).thenReturn(false);
        when(repository.findById(toSave.getId())).thenReturn(Optional.of(oldCategory));
        when(repository.save(toSave)).thenReturn(new Category(1L, "test"));

        Category actual = service.saveCategory(expected);
        assertEquals(expected, actual);

    }

    @Test
    void shouldThrowUniqueNameConstraintExceptionWhenCreateCategory() {
        Category toSave = new Category("test");

        when(repository.existsByName(toSave.getName())).thenReturn(true);
        Exception actualException = assertThrows(UniqueNameConstraintException.class, () -> service.saveCategory(toSave));

        String expectedExceptionMessage = "Category with test name already exist.";
        assertEquals(expectedExceptionMessage, actualException.getMessage());
    }

    @Test
    void shouldThrowUniqueNameConstraintExceptionWhenUpdateCategory() {
        Category toSave = new Category(1L, "test");

        when(repository.existsByName(toSave.getName())).thenReturn(true);
        when(repository.findById(toSave.getId())).thenReturn(Optional.of(new Category(1L, "2")));
        Exception actualException = assertThrows(UniqueNameConstraintException.class, () -> service.saveCategory(toSave));

        String expectedExceptionMessage = "Category with test name already exist.";
        assertEquals(expectedExceptionMessage, actualException.getMessage());
    }

}
