package com.dekk.category.presentation.response;

import com.dekk.category.application.dto.CategoryListResult;
import java.util.List;

public record CategoryListResponse(Long categoryId, String name, List<ChildCategoryResponse> children) {
    public static CategoryListResponse from(CategoryListResult result) {
        List<ChildCategoryResponse> childResponses =
                result.children().stream().map(ChildCategoryResponse::from).toList();
        return new CategoryListResponse(result.categoryId(), result.name(), childResponses);
    }

    public record ChildCategoryResponse(Long categoryId, String name) {
        public static ChildCategoryResponse from(CategoryListResult.ChildCategoryResult result) {
            return new ChildCategoryResponse(result.categoryId(), result.name());
        }
    }
}
