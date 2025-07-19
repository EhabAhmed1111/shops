package com.e_commerceapp.clothshops.service.category;

import com.e_commerceapp.clothshops.exceptionhandler.AlreadyExistException;
import com.e_commerceapp.clothshops.exceptionhandler.GlobalNotFoundException;
import com.e_commerceapp.clothshops.data.model.Category;
import com.e_commerceapp.clothshops.data.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CategoryService  {


    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category getCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId).orElseThrow(
                () -> new GlobalNotFoundException(
                        "there is no category with this id:" + categoryId
                )
        );
    }

    public Category getCategoryByName(String categoryName) {
        return Optional.ofNullable(categoryRepository.findByName(categoryName)).orElseThrow(
                () -> new GlobalNotFoundException(
                        "there is no category with this name:" + categoryName
                )
        );
    }

    public List<Category> getAllCategory() {
        return categoryRepository.findAll();
    }

    public Category addCategory(Category category) {

        return Optional.of(category)
                .filter(theAddedCategory -> !categoryRepository.existsByName(theAddedCategory.getName())).map(
                        categoryRepository::save
                ).orElseThrow(
                        () -> new AlreadyExistException(
                                "there is  category with this name already: " + category.getName()
                        )
                );
    }

    public Category updateCategory(Category category, Long categoryId) {

        return Optional.ofNullable(getCategoryById(categoryId)).map(
                existedCategory -> {
                    existedCategory.setName(category.getName());
                    return categoryRepository.save(existedCategory);
                }
        ).orElseThrow(
                () -> new GlobalNotFoundException(
                        "there is no category with this id:" + categoryId
                ));
    }

    public void deleteCategoryById(Long categoryId) {
        categoryRepository.findById(categoryId).ifPresentOrElse(categoryRepository::delete,
                () -> {
                    throw new GlobalNotFoundException(
                            "there is no category with this id:" + categoryId
                    );
                }
        );
    }
}
