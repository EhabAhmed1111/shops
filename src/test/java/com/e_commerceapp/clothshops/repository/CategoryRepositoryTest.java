package com.e_commerceapp.clothshops.repository;

import com.e_commerceapp.clothshops.data.model.Category;
import com.e_commerceapp.clothshops.data.repository.CategoryRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository underTest;

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }

    @Test
    void findByName() {
        //given
        String name = "Dell";
        Category category = new Category(name);
        //when
        Category actual = underTest.findByName(name);
        //then

        assertThat(actual).isEqualTo(category);
    }

    @Test
    void existsByName() {
    }
}