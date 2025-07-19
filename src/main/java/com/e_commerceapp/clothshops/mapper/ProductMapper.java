package com.e_commerceapp.clothshops.mapper;

import com.e_commerceapp.clothshops.data.model.Product;
import com.e_commerceapp.clothshops.data.dto.ProductDTO;
import com.e_commerceapp.clothshops.rest.requests.ProductRequests;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring",uses ={ImageMapper.class})
public interface ProductMapper {

    Product createProductFromProductReq(ProductRequests request);

    @Mapping(source = "images",target = "imageDTOList")
    ProductDTO createProductDTOFromProduct(Product product);

    @Mapping(source = "images",target = "imageDTOList")
    List<ProductDTO> createListOfProductDTOFromListOfProduct(List<Product> product);

}
