package com.e_commerceapp.clothshops.controller;


import com.e_commerceapp.clothshops.service.product.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/${api.prefix}/products")
public class ProductRestController {

    private final IProductService productService;
}
