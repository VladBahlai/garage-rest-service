package com.github.vladbahlai.garage.repository;

import com.github.vladbahlai.garage.model.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class CategoryRepositoryTest {

    @Autowired
    CategoryRepository repository;

    @Test
    void shouldReadAndWriteCategory() {
        Category expected = repository.save(new Category("test"));
        Optional<Category> actual = repository.findById(expected.getId());

        assertTrue(actual.isPresent());
        assertEquals(expected, actual.get());
    }
}
