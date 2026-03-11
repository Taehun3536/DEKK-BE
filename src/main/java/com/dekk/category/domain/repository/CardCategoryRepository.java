package com.dekk.category.domain.repository;

import java.util.List;

public interface CardCategoryRepository {
    void softDeleteAllByCategoryId(Long categoryId);

    void softDeleteAllByCategoryIdIn(List<Long> categoryIds);
}
