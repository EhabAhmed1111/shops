package com.e_commerceapp.clothshops.mapper;

import com.e_commerceapp.clothshops.dto.OrderItemDTO;
import com.e_commerceapp.clothshops.model.OrderItem;
import com.e_commerceapp.clothshops.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.math.BigDecimal;
import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {


    @Mappings({
            @Mapping(source ="product.id" ,target = "productId"),
            @Mapping(source ="product.name" ,target = "productName"),
            @Mapping(source ="product.brand" ,target = "productBrand")
    })
    OrderItemDTO createOrderItemDTO(OrderItem orderItem);


}
//OrderItemDTO createOrderItemDTO(OrderItem orderItem, Product product){
////    private int quantity;
////    private BigDecimal price;
////    private Long productId;
////    private String productName;
////
//    OrderItemDTO orderItemDTO = new OrderItemDTO();
//    orderItemDTO.setProductId(product.getId());
//    orderItemDTO.setQuantity(orderItem.getQuantity());
//    orderItemDTO.setPrice(orderItem.getPrice());
//    orderItemDTO.setProductName(product.getName());
//return orderItemDTO;
//}
//List<OrderItemDTO> createListOrderItemDTO(List<OrderItem> orderItem, Product product){
////    private int quantity;
////    private BigDecimal price;
////    private Long productId;
////    private String productName;
////
//   return orderItem.stream().map(order ->{
//        OrderItemDTO orderItemDTO = new OrderItemDTO();
//        orderItemDTO.setProductId(product.getId());
//        orderItemDTO.setQuantity(order.getQuantity());
//        orderItemDTO.setPrice(order.getPrice());
//        orderItemDTO.setProductName(product.getName());
//        return orderItemDTO;
//    }).toList();
//
//}