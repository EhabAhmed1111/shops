package com.e_commerceapp.clothshops.rest.controller;


import com.e_commerceapp.clothshops.exceptionhandler.BadRequestException;
import com.e_commerceapp.clothshops.exceptionhandler.GlobalNotFoundException;
import com.e_commerceapp.clothshops.data.model.Category;
import com.e_commerceapp.clothshops.rest.response.ApiResponse;
import com.e_commerceapp.clothshops.service.category.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

//@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/categories")
public class CategoryRestController {

    private final CategoryService categoryService;

    public CategoryRestController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllCategories() {
        try {
            List<Category> categories = categoryService.getAllCategory();
            return ResponseEntity.ok(new ApiResponse("Found!", categories));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), INTERNAL_SERVER_ERROR));
        }
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addCategory(@RequestBody Category category){
        try{
            if (category == null){
                throw new BadRequestException("enter some data");
            }
            Category newCategory = categoryService.addCategory(category);
            return ResponseEntity.ok(new ApiResponse("Added successfully" , newCategory));
        }catch (Exception e){
            return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(), CONFLICT));
        }
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<ApiResponse> findCategoryById(@PathVariable Long categoryId){
        try{
            if (categoryId == null){
                throw new BadRequestException("Enter categoryId");
            }
            Category category = categoryService.getCategoryById(categoryId);
                return ResponseEntity.ok(new ApiResponse("Found!" , category));
        }catch (GlobalNotFoundException e){
             throw new GlobalNotFoundException("There is no category with id: " + categoryId);
        }
    }

    @GetMapping("/category/{categoryName}")
    public ResponseEntity<ApiResponse> findCategoryByName(@PathVariable String categoryName){
        try{
            if (categoryName == null){
                throw new BadRequestException("Enter categoryName");
            }
            Category category = categoryService.getCategoryByName(categoryName);
            return ResponseEntity.ok(new ApiResponse("Found!" , category));
        }catch (GlobalNotFoundException e){
            throw new GlobalNotFoundException("There is no category with id: " + categoryName);
        }
    }

    @DeleteMapping("/category/{categoryId}/delete")
    public ResponseEntity<ApiResponse> deleteCategoryById(@PathVariable Long categoryId){
        try{
            if (categoryId == null){
                throw new BadRequestException("Enter categoryName");
            }
             categoryService.deleteCategoryById(categoryId);
            return ResponseEntity.ok(new ApiResponse("Category Deleted" , null));
        }catch (GlobalNotFoundException e){
            throw new GlobalNotFoundException("There is no category with id: " + categoryId);
        }
    }

    @PutMapping("/category/{categoryId}/update")
    public ResponseEntity<ApiResponse> updateCategory(@PathVariable Long categoryId, @RequestBody Category category){
        try{
            if (categoryId == null || category == null){
                throw new BadRequestException("Enter categoryName");
            }
            Category newCategory = categoryService.updateCategory(category, categoryId);
            return ResponseEntity.ok(new ApiResponse("Category Updated" , newCategory));
        }catch (GlobalNotFoundException e){
            throw new GlobalNotFoundException("There is no category with id: " + categoryId);
        }
    }

}
