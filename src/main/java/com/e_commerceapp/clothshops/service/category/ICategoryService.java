package com.e_commerceapp.clothshops.service.category;

import com.e_commerceapp.clothshops.model.Category;

import java.util.List;

public interface ICategoryService {

    Category getCategoryById(Long categoryId);

    Category getCategoryByName(String categoryName);

    List<Category> getAllCategory();

    Category addCategory(Category category);

    Category updateCategory(Category category,Long categoryId);

    void deleteCategoryById(Long categoryId);


}
