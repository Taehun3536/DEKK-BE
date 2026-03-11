package com.dekk.category.application;

import com.dekk.category.application.dto.CategoryListResult;
import com.dekk.category.domain.repository.CategoryRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryQueryService {

    private final CategoryRepository categoryRepository;

    public List<CategoryListResult> getCategoryTree() {
        return categoryRepository.findAllParentsWithChildren().stream()
                .map(CategoryListResult::from)
                .toList();
    }
}
