package com.github.vladbahlai.garage.service;


import com.github.javafaker.Faker;
import com.github.vladbahlai.garage.exception.UniqueNameConstraintException;
import com.github.vladbahlai.garage.model.Brand;
import com.github.vladbahlai.garage.repository.BrandRepository;
import com.github.vladbahlai.garage.service.impl.BrandServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = BrandServiceImpl.class)
public class BrandServiceTest {

    @MockBean
    BrandRepository repository;
    @Autowired
    BrandService service;

    @Test
    void shouldCreateBrand() {
        Brand toSave = new Brand("test");
        Brand expected = new Brand(1L, "test");

        when(repository.existsByName(toSave.getName())).thenReturn(false);
        when(repository.save(toSave)).thenReturn(new Brand(1L, "test"));

        Brand actual = service.saveBrand(toSave);
        assertEquals(expected, actual);

    }

    @Test
    void shouldUpdateBrand() {
        Brand expected = new Brand(1L, "test");
        Brand oldBrand = new Brand(1L, "test2");
        Brand toSave = new Brand(1L, "test");

        when(repository.existsByName(toSave.getName())).thenReturn(false);
        when(repository.findById(toSave.getId())).thenReturn(Optional.of(oldBrand));
        when(repository.save(toSave)).thenReturn(new Brand(1L, "test"));

        Brand actual = service.saveBrand(expected);
        assertEquals(expected, actual);

    }

    @Test
    void shouldThrowUniqueNameConstraintExceptionWhenCreateBrand() {
        Brand toSave = new Brand("test");

        when(repository.existsByName(toSave.getName())).thenReturn(true);
        Exception actualException = assertThrows(UniqueNameConstraintException.class, () -> service.saveBrand(toSave));

        String expectedExceptionMessage = "Brand with test name already exist.";
        assertEquals(expectedExceptionMessage, actualException.getMessage());
    }

    @Test
    void shouldThrowUniqueNameConstraintExceptionWhenUpdateBrand() {
        Brand toSave = new Brand(1L, "test");

        when(repository.existsByName(toSave.getName())).thenReturn(true);
        when(repository.findById(toSave.getId())).thenReturn(Optional.of(new Brand(1L, "2")));
        Exception actualException = assertThrows(UniqueNameConstraintException.class, () -> service.saveBrand(toSave));

        String expectedExceptionMessage = "Brand with test name already exist.";
        assertEquals(expectedExceptionMessage, actualException.getMessage());
    }

}
