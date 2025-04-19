package com.e_commerceapp.clothshops.controller;


import com.e_commerceapp.clothshops.exceptionhandler.AlreadyExistException;
import com.e_commerceapp.clothshops.exceptionhandler.BadRequestException;
import com.e_commerceapp.clothshops.exceptionhandler.GlobalNotFoundException;
import com.e_commerceapp.clothshops.model.Category;
import com.e_commerceapp.clothshops.response.ApiResponse;
import com.e_commerceapp.clothshops.service.category.ICategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RequiredArgsConstructor
@RestController
@RequestMapping("/${api.prefix}/categories")
public class CategoryRestController {

    private final ICategoryService categoryService;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllCategories() {
        try {
            List<Category> categories = categoryService.getAllCategory();
            return ResponseEntity.ok(new ApiResponse("Found!", categories));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Error:", INTERNAL_SERVER_ERROR));
//            throw new RuntimeException(e.getMessage(),e);
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
//             throw new AlreadyExistException(e.getMessage());
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
//            return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(), CONFLICT));
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
//            return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(), CONFLICT));
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
//            return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(), CONFLICT));
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
//            return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(), CONFLICT));
            throw new GlobalNotFoundException("There is no category with id: " + categoryId);
        }
    }

}
