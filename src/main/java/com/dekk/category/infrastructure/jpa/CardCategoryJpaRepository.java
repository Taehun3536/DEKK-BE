package com.dekk.category.infrastructure.jpa;

import com.dekk.category.domain.model.CardCategory;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CardCategoryJpaRepository extends JpaRepository<CardCategory, Long> {

    @Modifying(clearAutomatically = true)
    @Query(
            "UPDATE CardCategory cc SET cc.deletedAt = CURRENT_TIMESTAMP WHERE cc.categoryId = :categoryId AND cc.deletedAt IS NULL")
    void softDeleteAllByCategoryId(@Param("categoryId") Long categoryId);

    @Modifying(clearAutomatically = true)
    @Query(
            "UPDATE CardCategory cc SET cc.deletedAt = CURRENT_TIMESTAMP WHERE cc.categoryId IN :categoryIds AND cc.deletedAt IS NULL")
    void softDeleteAllByCategoryIdIn(@Param("categoryIds") List<Long> categoryIds);
}
