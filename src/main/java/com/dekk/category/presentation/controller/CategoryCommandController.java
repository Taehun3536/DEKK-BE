package com.dekk.category.presentation.controller;

import com.dekk.category.application.CategoryCommandService;
import com.dekk.category.presentation.request.CreateCategoryRequest;
import com.dekk.category.presentation.request.UpdateCategoryNameRequest;
import com.dekk.category.presentation.response.CategoryResultCode;
import com.dekk.category.presentation.response.CreateCategoryResponse;
import com.dekk.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/adm/v1/categories")
@RequiredArgsConstructor
public class CategoryCommandController implements CategoryCommandApi {

    private final CategoryCommandService categoryCommandService;

    @Override
    @PostMapping
    public ResponseEntity<ApiResponse<CreateCategoryResponse>> createParentCategory(
            @Valid @RequestBody CreateCategoryRequest request) {
        Long categoryId = categoryCommandService.createParentCategory(request.toCommand());
        return ResponseEntity.status(CategoryResultCode.CATEGORY_CREATED.status())
                .body(ApiResponse.of(CategoryResultCode.CATEGORY_CREATED, new CreateCategoryResponse(categoryId)));
    }

    @Override
    @PostMapping("/{parentId}/sub")
    public ResponseEntity<ApiResponse<CreateCategoryResponse>> createChildCategory(
            @PathVariable("parentId") Long parentId, @Valid @RequestBody CreateCategoryRequest request) {
        Long categoryId = categoryCommandService.createChildCategory(parentId, request.toCommand());
        return ResponseEntity.status(CategoryResultCode.CATEGORY_CREATED.status())
                .body(ApiResponse.of(CategoryResultCode.CATEGORY_CREATED, new CreateCategoryResponse(categoryId)));
    }

    @Override
    @PatchMapping("/{categoryId}")
    public ResponseEntity<ApiResponse<Void>> updateCategoryName(
            @PathVariable("categoryId") Long categoryId, @Valid @RequestBody UpdateCategoryNameRequest request) {
        categoryCommandService.updateCategoryName(categoryId, request.toCommand());
        return ResponseEntity.ok(ApiResponse.from(CategoryResultCode.CATEGORY_NAME_UPDATED));
    }

    @Override
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<ApiResponse<Void>> deleteParentCategory(@PathVariable("categoryId") Long categoryId) {
        categoryCommandService.deleteParentCategory(categoryId);
        return ResponseEntity.ok(ApiResponse.from(CategoryResultCode.CATEGORY_DELETED));
    }

    @Override
    @DeleteMapping("/sub/{categoryId}")
    public ResponseEntity<ApiResponse<Void>> deleteChildCategory(@PathVariable("categoryId") Long categoryId) {
        categoryCommandService.deleteChildCategory(categoryId);
        return ResponseEntity.ok(ApiResponse.from(CategoryResultCode.CATEGORY_DELETED));
    }
}
