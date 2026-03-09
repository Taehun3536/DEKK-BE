package com.dekk.category.infrastructure.jpa;

import com.dekk.category.domain.model.Category;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryJpaRepository extends JpaRepository<Category, Long> {

    @EntityGraph(attributePaths = "children")
    @Query("SELECT c FROM Category c WHERE c.parent IS NULL ORDER BY c.id")
    List<Category> findAllParentsWithChildren();
}
