package com.dekk.category.application;

import com.dekk.category.application.command.CreateCategoryCommand;
import com.dekk.category.application.command.UpdateCategoryNameCommand;
import com.dekk.category.domain.exception.CategoryBusinessException;
import com.dekk.category.domain.exception.CategoryErrorCode;
import com.dekk.category.domain.model.Category;
import com.dekk.category.domain.repository.CardCategoryRepository;
import com.dekk.category.domain.repository.CategoryRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryCommandService {

    private final CategoryRepository categoryRepository;
    private final CardCategoryRepository cardCategoryRepository;

    public Long createParentCategory(CreateCategoryCommand command) {
        Category parent = Category.createParent(command.name());
        Category savedCategory = categoryRepository.save(parent);
        return savedCategory.getId();
    }

    public Long createChildCategory(Long parentId, CreateCategoryCommand command) {
        Category parent = categoryRepository
                .findById(parentId)
                .orElseThrow(() -> new CategoryBusinessException(CategoryErrorCode.CATEGORY_NOT_FOUND));

        Category child = Category.createChild(parent, command.name());
        Category savedCategory = categoryRepository.save(child);
        return savedCategory.getId();
    }

    public void updateCategoryName(Long categoryId, UpdateCategoryNameCommand command) {
        Category category = categoryRepository
                .findById(categoryId)
                .orElseThrow(() -> new CategoryBusinessException(CategoryErrorCode.CATEGORY_NOT_FOUND));

        category.updateName(command.name());
    }

    public void deleteParentCategory(Long categoryId) {
        Category category = categoryRepository
                .findById(categoryId)
                .orElseThrow(() -> new CategoryBusinessException(CategoryErrorCode.CATEGORY_NOT_FOUND));

        List<Long> childIds =
                category.getChildren().stream().map(Category::getId).toList();

        if (!childIds.isEmpty()) {
            cardCategoryRepository.softDeleteAllByCategoryIdIn(childIds);
            categoryRepository.softDeleteAllByParentId(categoryId);
        }

        cardCategoryRepository.softDeleteAllByCategoryId(categoryId);
        categoryRepository.delete(category);
    }

    public void deleteChildCategory(Long categoryId) {
        Category category = categoryRepository
                .findById(categoryId)
                .orElseThrow(() -> new CategoryBusinessException(CategoryErrorCode.CATEGORY_NOT_FOUND));

        cardCategoryRepository.softDeleteAllByCategoryId(categoryId);
        categoryRepository.delete(category);
    }
}
