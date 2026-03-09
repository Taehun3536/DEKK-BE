package com.dekk.category.domain.repository;

import com.dekk.category.domain.model.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository {
    Category save(Category category);
    Optional<Category> findById(Long id);
    List<Category> findAllParentsWithChildren();
}
