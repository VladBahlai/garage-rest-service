package com.github.vladbahlai.garage.mapper;

import com.github.vladbahlai.garage.dto.BrandDTO;
import com.github.vladbahlai.garage.model.Brand;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = BrandMapper.class)
class BrandMapperTest {

    @Autowired
    BrandMapper mapper;

    @Test
    void shouldConvertToBrandDto() {
        Brand brand = new Brand("test");
        BrandDTO expected = new BrandDTO("test");
        BrandDTO actual = mapper.toBrandDTO(brand);
        assertEquals(expected, actual);
    }


    @Test
    void shouldConvertToBrand() {
        BrandDTO brandDto = new BrandDTO("test");
        Brand expected = new Brand("test");
        Brand actual = mapper.toBrand(brandDto);
        assertEquals(expected, actual);
    }
}
