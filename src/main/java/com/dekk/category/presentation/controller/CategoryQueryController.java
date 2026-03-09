package com.dekk.category.presentation.controller;

import com.dekk.category.application.CategoryQueryService;
import com.dekk.category.presentation.response.CategoryResultCode;
import com.dekk.category.presentation.response.CategoryListResponse;
import com.dekk.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/adm/v1/categories")
@RequiredArgsConstructor
public class CategoryQueryController implements CategoryQueryApi {

    private final CategoryQueryService categoryQueryService;

    @Override
    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoryListResponse>>> getCategoryTree() {
        List<CategoryListResponse> response = categoryQueryService.getCategoryTree().stream()
                .map(CategoryListResponse::from)
                .toList();
        return ResponseEntity.ok(
                ApiResponse.of(CategoryResultCode.CATEGORY_TREE_FETCHED, response)
        );
    }
}
