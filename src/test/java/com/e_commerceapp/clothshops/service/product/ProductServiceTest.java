package com.e_commerceapp.clothshops.service.product;

import com.e_commerceapp.clothshops.mapper.ProductMapper;
import com.e_commerceapp.clothshops.model.Product;
import com.e_commerceapp.clothshops.repository.CategoryRepository;
import com.e_commerceapp.clothshops.repository.ImageRepository;
import com.e_commerceapp.clothshops.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private ProductMapper productMapper;

    private ProductService underTest;

    @BeforeEach
    void setUp() {
        underTest = new ProductService(productRepository,categoryRepository,productMapper);
    }

    @Test
    @Disabled
    void addProduct() {

    }

    @Test
    @Disabled
    void getProductById() {
    }

    @Test
    @Disabled
    void deleteProductById() {
    }

    @Test
    @Disabled
    void updateProduct() {
    }

    @Test
    void canGetAllProducts() {
        //when
        productRepository.findAll();
        //then
        verify(productRepository).findAll();
    }
    @Test
    void canConvertAllProductToListOfProductDTO() {

        //given
        List<Product> products = productRepository.findAll();

        ArgumentCaptor<Product> argumentCaptor = ArgumentCaptor.forClass(Product.class);
        //when
        productMapper.createListOfProductDTOFromListOfProduct(products);
        //then
        verify(productMapper).createListOfProductDTOFromListOfProduct(argumentCaptor.getAllValues());
        assertThat(products).isEqualTo(argumentCaptor.getAllValues());
    }

    @Test
    @Disabled
    void getAllProductsByCategoryName() {
    }

    @Test
    @Disabled
    void getAllProductsByBrandName() {
    }

    @Test
    @Disabled
    void getAllProductsByCategoryNameAndBrandName() {
    }

    @Test
    @Disabled
    void getProductsByName() {
    }

    @Test
    @Disabled
    void getProductsByBrandNameAndProductName() {
    }

    @Test
    @Disabled
    void getProductsByCategoryNameAndProductName() {
    }

    @Test
    @Disabled
    void countProductByBrandNameAndProductName() {
    }

    @Test
    @Disabled
    void countProductByCategoryNameAndProductName() {
    }
}