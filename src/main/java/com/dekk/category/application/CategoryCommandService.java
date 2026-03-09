package com.dekk.category.application;

import com.dekk.category.application.command.CreateCategoryCommand;
import com.dekk.category.domain.exception.CategoryBusinessException;
import com.dekk.category.domain.exception.CategoryErrorCode;
import com.dekk.category.domain.model.Category;
import com.dekk.category.domain.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryCommandService {

    private final CategoryRepository categoryRepository;

    public Long createParentCategory(CreateCategoryCommand command) {
        Category parent = Category.createParent(command.name());
        Category savedCategory = categoryRepository.save(parent);
        return savedCategory.getId();
    }

    public Long createChildCategory(Long parentId, CreateCategoryCommand command) {
        Category parent = categoryRepository.findById(parentId)
                .orElseThrow(() -> new CategoryBusinessException(CategoryErrorCode.CATEGORY_NOT_FOUND));

        Category child = Category.createChild(parent, command.name());
        Category savedCategory = categoryRepository.save(child);
        return savedCategory.getId();
    }
}
