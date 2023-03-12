package com.github.vladbahlai.garage.mapper;

import com.github.vladbahlai.garage.dto.BrandDTO;
import com.github.vladbahlai.garage.dto.ModelDTO;
import com.github.vladbahlai.garage.model.Brand;
import com.github.vladbahlai.garage.model.Model;
import com.github.vladbahlai.garage.service.BrandService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = ModelMapper.class)
class ModelMapperTest {

    @MockBean
    BrandService brandService;
    @Autowired
    ModelMapper mapper;

    @Test
    void shouldConvertToModelDto() {
        Model model = new Model(1L, "test", new Brand(1L, "test"));
        ModelDTO expected = new ModelDTO("test", new BrandDTO("test"));
        ModelDTO actual = mapper.toModelDTO(model);
        assertEquals(expected, actual);
    }

    @Test
    void shouldConvertToModel() {
        ModelDTO modelDto = new ModelDTO("test", new BrandDTO("test"));

        when(brandService.getBrandByName(modelDto.getBrand().getName())).thenReturn(new Brand(1L, "test"));
        Model expected = new Model("test", new Brand(1L, "test"));
        Model actual = mapper.toModel(modelDto);

        assertEquals(expected, actual);
    }

}
