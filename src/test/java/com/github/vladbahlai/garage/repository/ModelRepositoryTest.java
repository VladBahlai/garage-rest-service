package com.github.vladbahlai.garage.repository;

import com.github.vladbahlai.garage.model.Brand;
import com.github.vladbahlai.garage.model.Model;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class ModelRepositoryTest {

    @Autowired
    ModelRepository repository;
    @Autowired
    BrandRepository brandRepository;

    @Test
    void shouldReadAndWriteModel() {
        Brand brand = brandRepository.save(new Brand("test"));
        Model expected = repository.save(new Model("test", brand));
        Optional<Model> actual = repository.findById(expected.getId());
        assertTrue(actual.isPresent());
        assertEquals(expected, actual.get());
    }


}
