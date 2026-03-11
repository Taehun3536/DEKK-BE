package com.dekk.category.application.dto;

import com.dekk.category.domain.model.Category;
import java.util.List;

public record CategoryListResult(Long categoryId, String name, List<ChildCategoryResult> children) {
    public static CategoryListResult from(Category parent) {
        List<ChildCategoryResult> childResults =
                parent.getChildren().stream().map(ChildCategoryResult::from).toList();
        return new CategoryListResult(parent.getId(), parent.getName(), childResults);
    }

    public record ChildCategoryResult(Long categoryId, String name) {
        public static ChildCategoryResult from(Category child) {
            return new ChildCategoryResult(child.getId(), child.getName());
        }
    }
}
