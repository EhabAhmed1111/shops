package com.e_commerceapp.clothshops.mapper;

import com.e_commerceapp.clothshops.model.Category;
import com.e_commerceapp.clothshops.model.Product;
import com.e_commerceapp.clothshops.dto.ProductDTO;
import com.e_commerceapp.clothshops.requests.ProductRequests;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Mapper(componentModel = "spring",uses ={ImageMapper.class})
public interface ProductMapper {

//    @Mappings(
//            {
//                    @Mapping(source = "id", target = "id"),
//                    @Mapping(source = "name", target = "name"),
//                    @Mapping(source = "brand", target = "brand"),
//                    @Mapping(source = "price", target = "price"),
//                    @Mapping(source = "inventory", target = "inventory"),
//                    @Mapping(source = "description", target = "description"),
//                    @Mapping(source = "category", target = "category")
//            }
//    )
    Product createProductFromProductReq(ProductRequests request);


//    @Mapping(source = "images",target = "imageDTOList")
//    Product createProductFromProductDto(ProductDTO productDTO);

    @Mapping(source = "images",target = "imageDTOList")
    ProductDTO createProductDTOFromProduct(Product product);


    @Mapping(source = "images",target = "imageDTOList")
    List<ProductDTO> createListOfProductDTOFromListOfProduct(List<Product> product);




//    ProductDTO createProductDTOFromProduct(Product request);
}
