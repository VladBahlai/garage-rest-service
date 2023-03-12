package com.github.vladbahlai.garage.repository;

import com.github.vladbahlai.garage.model.Brand;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class BrandRepositoryTest {

    @Autowired
    BrandRepository repository;

    @Test
    void shouldReadAndWriteBrand() {
        Brand expected = repository.save(new Brand("test"));
        Optional<Brand> actual = repository.findById(expected.getId());

        assertTrue(actual.isPresent());
        assertEquals(expected, actual.get());
    }
}
