package com.e_commerceapp.clothshops.controller;


import com.e_commerceapp.clothshops.dto.ProductDTO;
import com.e_commerceapp.clothshops.exceptionhandler.GlobalNotFoundException;
import com.e_commerceapp.clothshops.model.Product;
import com.e_commerceapp.clothshops.response.ApiResponse;
import com.e_commerceapp.clothshops.service.product.IProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

//@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/products")
public class ProductRestController {

    private final IProductService productService;

    public ProductRestController(IProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllProduct() {
        List<Product> products = productService.getAllProducts();
        return ResponseEntity.ok(new ApiResponse("success", products));
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<ApiResponse> getProductById(@PathVariable Long productId) {
        try {
            Product product = productService.getProductById(productId);
            return ResponseEntity.ok(new ApiResponse("success", product));
        } catch (GlobalNotFoundException e) {
//            throw new GlobalNotFoundException("Failed!");
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("There is no product with id: " + productId, null));
        }
    }

    @GetMapping("/product/{categoryName}/all/products")
    public ResponseEntity<ApiResponse> getAllProductsByCategoryName(@PathVariable String categoryName) {
        try {
            List<Product> products = productService.getAllProductsByCategoryName(categoryName);
            if (products.isEmpty()){
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("There no category with name: " + categoryName, null));
            }
            return ResponseEntity.ok(new ApiResponse("success", products));
        } catch (Exception e) {
//            throw new GlobalNotFoundException("Failed!");
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), INTERNAL_SERVER_ERROR));

        }
    }

    @GetMapping("/{productName}/products")
    public ResponseEntity<ApiResponse> getProductsByName(@PathVariable String productName) {
        try {
            List<Product> products = productService.getProductsByName(productName);
            if (products.isEmpty()){
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("There no product with name: " + productName, null));
            }
            return ResponseEntity.ok(new ApiResponse("success", products));
        } catch (Exception e) {
//            throw new GlobalNotFoundException("Failed!");
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), INTERNAL_SERVER_ERROR));

        }
    }

    @GetMapping("/by-brandName")
    public ResponseEntity<ApiResponse> getAllProductWithBrandName(@RequestParam String brandName) {
        try {
            List<Product> products = productService.getAllProductsByBrandName(brandName);
            if (products.isEmpty()){
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("There no brand with name: " + brandName, null));
            }
            return ResponseEntity.ok(new ApiResponse("success", products));
        } catch (Exception e) {
//            throw new GlobalNotFoundException("Failed!");
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), INTERNAL_SERVER_ERROR));

        }
    }

    @GetMapping("/by/brandName-and-categoryName")
    public ResponseEntity<ApiResponse> getAllProductsByCategoryNameAndBrandName(@RequestParam String brandName,
                                                                                @RequestParam String categoryName) {
        try {
            List<Product> products = productService.getAllProductsByCategoryNameAndBrandName(categoryName, brandName);
            if (products.isEmpty()){
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("There no brand with name: " + brandName + "or category with name: " + categoryName, null));

            }
            return ResponseEntity.ok(new ApiResponse("success", products));
        } catch (Exception e) {
//            throw new GlobalNotFoundException("Failed!");
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), INTERNAL_SERVER_ERROR));

        }
    }

    @GetMapping("/by/productName-and-categoryName")
    public ResponseEntity<ApiResponse> getProductsByCategoryNameAndProductName(@RequestParam String productName,
                                                                               @RequestParam String categoryName) {
        try {
            List<Product> products = productService.getProductsByCategoryNameAndProductName(productName, categoryName);
            if (products.isEmpty()){
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("There no product with name: " + productName + "or category with name: " + categoryName, null));
            }
            return ResponseEntity.ok(new ApiResponse("success", products));
        } catch (Exception e) {
//            throw new GlobalNotFoundException("Failed!");
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), INTERNAL_SERVER_ERROR));

        }
    }

    @GetMapping("/by/brandName-and-productName")
    public ResponseEntity<ApiResponse> getProductsByBrandNameAndProductName(@RequestParam String brandName, @RequestParam String productName) {
        try {
            List<Product> products = productService.getProductsByBrandNameAndProductName(productName, brandName);
            if (products.isEmpty()){
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("There no brand with name: " + brandName + "or product with name: " + productName, null));
            }
            return ResponseEntity.ok(new ApiResponse("success", products));
        } catch (Exception e) {
//            throw new GlobalNotFoundException("Failed!");
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), INTERNAL_SERVER_ERROR));

        }
    }

    @GetMapping("/product/count/by-brandName/and-productName")
    public ResponseEntity<ApiResponse> countProductByBrandNameAndProductName(@RequestParam String brandName,
                                                                             @RequestParam String productName) {
        try {
            var productCount = productService.countProductByBrandNameAndProductName(brandName, productName);
            return ResponseEntity.ok(new ApiResponse("product count!", productCount));
        } catch (GlobalNotFoundException e) {
//            throw new GlobalNotFoundException("Failed!");
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("There no brand with name: " + brandName + "or product with name: " + productName, null));
        }
    }

 @GetMapping("/product/count/by-categoryName/and-productName")
    public ResponseEntity<ApiResponse> countProductByCategoryNameAndProductName(@RequestParam String categoryName,
                                                                                @RequestParam String productName) {
        try {
            var productCount = productService.countProductByCategoryNameAndProductName(productName, categoryName);
            return ResponseEntity.ok(new ApiResponse("product count!", productCount));
        } catch (GlobalNotFoundException e) {
//            throw new GlobalNotFoundException("Failed!");
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("There no category with name: " + categoryName + "or product with name: " + productName, null));
        }
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addProduct(@RequestBody ProductDTO productDTO) {
        try {
            Product product = productService.addProduct(productDTO);
            return ResponseEntity.ok(new ApiResponse("Add product success", product));
        } catch (Exception e) {
//            throw new RuntimeException("failed",e);
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Failed to add product due to " + e.getMessage(), INTERNAL_SERVER_ERROR));
        }

    }

    @PutMapping("/product/{productId}/update")
    public ResponseEntity<ApiResponse> updateProduct(@RequestBody ProductDTO productDTO, @PathVariable Long productId) {
        try {
            Product product = productService.updateProduct(productDTO, productId);
            return ResponseEntity.ok(new ApiResponse("update product success", product));
        } catch (GlobalNotFoundException e) {
//                throw new GlobalNotFoundException("there no category with name: " + productDTO.getCategory().getName());
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("There is no product with id: " + productId, NOT_FOUND));
        }
    }

    @DeleteMapping("/product/{productId}/delete")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable Long productId) {
        try {
            productService.deleteProductById(productId);
            return ResponseEntity.ok(new ApiResponse("delete product success",
//            productId
                    null
            ));
        } catch (GlobalNotFoundException e) {
//                throw new GlobalNotFoundException("there no category with name: " + productDTO.getCategory().getName());
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("There is no product with id: " + productId, NOT_FOUND));
        }
    }


}
