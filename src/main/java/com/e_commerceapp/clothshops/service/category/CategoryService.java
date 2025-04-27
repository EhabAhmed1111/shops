package com.e_commerceapp.clothshops.service.category;

import com.e_commerceapp.clothshops.exceptionhandler.AlreadyExistException;
import com.e_commerceapp.clothshops.exceptionhandler.GlobalNotFoundException;
import com.e_commerceapp.clothshops.model.Category;
import com.e_commerceapp.clothshops.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
//@RequiredArgsConstructor
@Transactional
public class CategoryService implements ICategoryService {


    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Category getCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId).orElseThrow(
                () -> new GlobalNotFoundException(
                        "there is no category with this id:" + categoryId
                )
        );
    }

    @Override
    public Category getCategoryByName(String categoryName) {
        return Optional.ofNullable(categoryRepository.findByName(categoryName)).orElseThrow(
                () -> new GlobalNotFoundException(
                        "there is no category with this name:" + categoryName
                )
        );
    }

    @Override
    public List<Category> getAllCategory() {
        return categoryRepository.findAll();
    }

    @Override
    public Category addCategory(Category category) {

        //optional.of is used when we know that value will never be null
        //so why we use it
        return Optional.of(category)
                //if category exist then we filter it
                //and then the value will be null and will go to exception
                .filter(c -> !categoryRepository.existsByName(c.getName())).map(
                        categoryRepository::save
                ).orElseThrow(
                        () -> new AlreadyExistException(
                                "there is  category with this name already: " + category.getName()
                        )
                );
    }

    @Override
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

//        Category existedCategory = categoryRepository.findById(categoryId).orElseThrow(
//                () -> new GlobalNotFoundException(
//                        "there is no category with this id:" + categoryId
//                )
//        );
//        existedCategory.setName(category.getName());
//
//        return categoryRepository.save(existedCategory);
    }

    @Override
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
