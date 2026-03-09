package com.dekk.category.infrastructure;

import com.dekk.category.domain.model.Category;
import com.dekk.category.domain.repository.CategoryRepository;
import com.dekk.category.infrastructure.jpa.CategoryJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CategoryRepositoryImpl implements CategoryRepository {

    private final CategoryJpaRepository categoryJpaRepository;

    @Override
    public Category save(Category category) {
        return categoryJpaRepository.save(category);
    }

    @Override
    public Optional<Category> findById(Long id) {
        return categoryJpaRepository.findById(id);
    }

    @Override
    public List<Category> findAllParentsWithChildren() {
        return categoryJpaRepository.findAllParentsWithChildren();
    }
}
