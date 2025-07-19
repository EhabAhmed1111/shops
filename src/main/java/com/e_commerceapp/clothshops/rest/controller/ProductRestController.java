package com.e_commerceapp.clothshops.rest.controller;


import com.e_commerceapp.clothshops.data.dto.ProductDTO;
import com.e_commerceapp.clothshops.exceptionhandler.AlreadyExistException;
import com.e_commerceapp.clothshops.exceptionhandler.GlobalNotFoundException;
import com.e_commerceapp.clothshops.data.model.Product;
import com.e_commerceapp.clothshops.rest.requests.ProductRequests;
import com.e_commerceapp.clothshops.rest.response.ApiResponse;
import com.e_commerceapp.clothshops.service.product.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("${api.prefix}/products")
public class ProductRestController {

    private final ProductService productService;

    public ProductRestController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllProduct() {
        List<ProductDTO> productDTOs = productService.getAllProducts();
        return ResponseEntity.ok(new ApiResponse("success", productDTOs));
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<ApiResponse> getProductById(@PathVariable Long productId) {
        try {
            Product product = productService.getProductById(productId);
            return ResponseEntity.ok(new ApiResponse("success", product));
        } catch (GlobalNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("There is no product with id: " + productId, null));
        }
    }

    @GetMapping("/product/{categoryName}/all/products")
    public ResponseEntity<ApiResponse> getAllProductsByCategoryName(@PathVariable String categoryName) {
        try {
            List<ProductDTO> productDTOs = productService.getAllProductsByCategoryName(categoryName);
            if (productDTOs.isEmpty()){
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("There no category with name: " + categoryName, null));
            }
            return ResponseEntity.ok(new ApiResponse("success", productDTOs));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), INTERNAL_SERVER_ERROR));

        }
    }

    @GetMapping("/{productName}/products")
    public ResponseEntity<ApiResponse> getProductsByName(@PathVariable String productName) {
        try {
            List<ProductDTO> productDTOs = productService.getProductsByName(productName);
            if (productDTOs.isEmpty()){
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("There no product with name: " + productName, null));
            }
            return ResponseEntity.ok(new ApiResponse("success", productDTOs));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), INTERNAL_SERVER_ERROR));

        }
    }

    @GetMapping("/by-brandName")
    public ResponseEntity<ApiResponse> getAllProductWithBrandName(@RequestParam String brandName) {
        try {
            List<ProductDTO> productDTOs = productService.getAllProductsByBrandName(brandName);
            if (productDTOs.isEmpty()){
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("There no brand with name: " + brandName, null));
            }
            return ResponseEntity.ok(new ApiResponse("success", productDTOs));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), INTERNAL_SERVER_ERROR));

        }
    }

    @GetMapping("/by/brandName-and-categoryName")
    public ResponseEntity<ApiResponse> getAllProductsByCategoryNameAndBrandName(@RequestParam String brandName,
                                                                                @RequestParam String categoryName) {
        try {
            List<ProductDTO> productDTOs = productService.getAllProductsByCategoryNameAndBrandName(categoryName, brandName);
            if (productDTOs.isEmpty()){
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("There no brand with name: " + brandName + "or category with name: " + categoryName, null));

            }
            return ResponseEntity.ok(new ApiResponse("success", productDTOs));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), INTERNAL_SERVER_ERROR));

        }
    }

    @GetMapping("/by/productName-and-categoryName")
    public ResponseEntity<ApiResponse> getProductsByCategoryNameAndProductName(@RequestParam String productName,
                                                                               @RequestParam String categoryName) {
        try {
            List<ProductDTO> productDTOs = productService.getProductsByCategoryNameAndProductName(productName, categoryName);
            if (productDTOs.isEmpty()){
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("There no product with name: " + productName + "or category with name: " + categoryName, null));
            }
            return ResponseEntity.ok(new ApiResponse("success", productDTOs));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), INTERNAL_SERVER_ERROR));
        }
    }

    @GetMapping("/by/brandName-and-productName")
    public ResponseEntity<ApiResponse> getProductsByBrandNameAndProductName(@RequestParam String brandName, @RequestParam String productName) {
        try {
            List<ProductDTO> productDTOs = productService.getProductsByBrandNameAndProductName(productName, brandName);
            if (productDTOs.isEmpty()){
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("There no brand with name: " + brandName + "or product with name: " + productName, null));
            }
            return ResponseEntity.ok(new ApiResponse("success", productDTOs));
        } catch (Exception e) {
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
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("There no category with name: " + categoryName + "or product with name: " + productName, null));
        }
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addProduct(@RequestBody ProductRequests productRequests) {
        try {
            Product product = productService.addProduct(productRequests);
            return ResponseEntity.ok(new ApiResponse("Add product success", product));
        } catch (AlreadyExistException e) {
            return ResponseEntity.status(CONFLICT).body(new ApiResponse("Failed to add product due to " + e.getMessage(), CONFLICT));
        }

    }

    @PutMapping("/product/{productId}/update")
    public ResponseEntity<ApiResponse> updateProduct(@RequestBody ProductRequests productRequests, @PathVariable Long productId) {
        try {
            ProductDTO productDTO = productService.updateProduct(productRequests, productId);
            return ResponseEntity.ok(new ApiResponse("update product success", productDTO));
        } catch (GlobalNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("There is no product with id: " + productId, NOT_FOUND));
        }
    }

    @DeleteMapping("/product/{productId}/delete")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable Long productId) {
        try {
            productService.deleteProductById(productId);
            return ResponseEntity.ok(new ApiResponse("delete product success",
                    null
            ));
        } catch (GlobalNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("There is no product with id: " + productId, NOT_FOUND));
        }
    }


}
