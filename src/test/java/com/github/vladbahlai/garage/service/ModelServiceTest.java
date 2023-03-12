package com.github.vladbahlai.garage.service;

import com.github.vladbahlai.garage.exception.UniqueNameConstraintException;
import com.github.vladbahlai.garage.model.Model;
import com.github.vladbahlai.garage.repository.ModelRepository;
import com.github.vladbahlai.garage.service.impl.ModelServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = ModelServiceImpl.class)
public class ModelServiceTest {

    @MockBean
    ModelRepository repository;
    @Autowired
    ModelService service;

    @Test
    void shouldCreateModel() {
        Model toSave = new Model("test");
        Model expected = new Model(1L, "test");

        when(repository.existsByName(toSave.getName())).thenReturn(false);
        when(repository.save(toSave)).thenReturn(new Model(1L, "test"));

        Model actual = service.saveModel(toSave);
        assertEquals(expected, actual);

    }

    @Test
    void shouldUpdateModel() {
        Model expected = new Model(1L, "test");
        Model toSave = new Model(1L, "test");
        Model oldModel = new Model(1L, "test2");

        when(repository.existsByName(toSave.getName())).thenReturn(false);
        when(repository.findById(toSave.getId())).thenReturn(Optional.of(oldModel));
        when(repository.save(toSave)).thenReturn(toSave);

        Model actual = service.saveModel(expected);
        assertEquals(expected, actual);

    }

    @Test
    void shouldThrowUniqueNameConstraintExceptionWhenCreateModel() {
        Model toSave = new Model("test");

        when(repository.existsByName(toSave.getName())).thenReturn(true);
        Exception actualException = assertThrows(UniqueNameConstraintException.class, () -> service.saveModel(toSave));

        String expectedExceptionMessage = "Model with test name already exist.";
        assertEquals(expectedExceptionMessage, actualException.getMessage());
    }

    @Test
    void shouldThrowUniqueNameConstraintExceptionWhenUpdateModel() {
        Model toSave = new Model(1L, "test");

        when(repository.existsByName(toSave.getName())).thenReturn(true);
        when(repository.findById(toSave.getId())).thenReturn(Optional.of(new Model(1L, "2")));
        Exception actualException = assertThrows(UniqueNameConstraintException.class, () -> service.saveModel(toSave));

        String expectedExceptionMessage = "Model with test name already exist.";
        assertEquals(expectedExceptionMessage, actualException.getMessage());
    }

}
