package com.github.vladbahlai.garage.mapper;

import com.github.vladbahlai.garage.dto.BrandDTO;
import com.github.vladbahlai.garage.dto.CarDTO;
import com.github.vladbahlai.garage.dto.CategoryDTO;
import com.github.vladbahlai.garage.dto.ModelDTO;
import com.github.vladbahlai.garage.model.Brand;
import com.github.vladbahlai.garage.model.Car;
import com.github.vladbahlai.garage.model.Category;
import com.github.vladbahlai.garage.model.Model;
import com.github.vladbahlai.garage.service.BrandService;
import com.github.vladbahlai.garage.service.CategoryService;
import com.github.vladbahlai.garage.service.ModelService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = CarMapper.class)
public class CarMapperTest {

    @MockBean
    ModelMapper modelMapper;
    @MockBean
    CategoryMapper categoryMapper;
    @MockBean
    BrandService brandService;
    @MockBean
    CategoryService categoryService;
    @MockBean
    ModelService modelService;
    @Autowired
    CarMapper carMapper;

    @Test
    void shouldConvertToCarDto() {
        Brand brand = new Brand("testModel");
        Model model = new Model("testBrand", brand);
        Car car = new Car(brand, LocalDate.of(2002, 2, 2), model);

        ModelDTO modelDTO = new ModelDTO("testModel", new BrandDTO("testBrand"));

        car.getCategories().add(new Category(1L, "testCategory1"));
        car.getCategories().add(new Category(2L, "testCategory2"));
        List<Category> categories = new ArrayList<>(car.getCategories());
        Set<CategoryDTO> categoryDTOSet = new HashSet<>(Arrays.asList(new CategoryDTO("testCategory1"), new CategoryDTO("testCategory2")));

        CarDTO expected = new CarDTO(modelDTO.getBrand(), LocalDate.of(2002, 2, 2), modelDTO, categoryDTOSet);
        when(modelMapper.toModelDTO(car.getModel())).thenReturn(new ModelDTO("testModel", new BrandDTO("testBrand")));
        when(categoryMapper.toCategoryDTO(categories.get(0))).thenReturn(new CategoryDTO("testCategory1"));
        when(categoryMapper.toCategoryDTO(categories.get(1))).thenReturn(new CategoryDTO("testCategory2"));

        CarDTO actual = carMapper.toCarDto(car);
        assertEquals(expected, actual);

    }

    @Test
    void shouldConvertToCar() {
        Brand brand = new Brand(1L, "testModel");
        Model model = new Model("testBrand", brand);
        Car expected = new Car(brand, LocalDate.of(2002, 2, 2), model);
        expected.getCategories().add(new Category(1L, "testCategory1"));
        expected.getCategories().add(new Category(2L, "testCategory2"));

        ModelDTO modelDTO = new ModelDTO("testModel", new BrandDTO("testBrand"));
        Set<CategoryDTO> categoryDTOSet = new HashSet<>(Arrays.asList(new CategoryDTO("testCategory1"), new CategoryDTO("testCategory2")));
        CarDTO carDTO = new CarDTO(modelDTO.getBrand(), LocalDate.of(2002, 2, 2), modelDTO, categoryDTOSet);
        List<CategoryDTO> categoryDTOList = new ArrayList<>(categoryDTOSet);


        when(brandService.getBrandByName(carDTO.getBrand().getName())).thenReturn(brand);
        when(modelService.getModelByName(carDTO.getModel().getName())).thenReturn(model);
        when(categoryService.getCategoryByName(categoryDTOList.get(0).getName())).thenReturn(new Category(1L, "testCategory1"));
        when(categoryService.getCategoryByName(categoryDTOList.get(1).getName())).thenReturn(new Category(2L, "testCategory2"));

        Car actual = carMapper.toCar(carDTO);
        assertEquals(expected, actual);
        assertEquals(expected.getCategories(), actual.getCategories());
    }
}
