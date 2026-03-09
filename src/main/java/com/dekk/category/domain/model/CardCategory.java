package com.dekk.category.domain.model;

import com.dekk.category.domain.exception.CategoryBusinessException;
import com.dekk.category.domain.exception.CategoryErrorCode;
import com.dekk.common.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.SQLDelete;

@Entity
@Table(name = "card_categories")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE card_categories SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@Filter(name = "deletedFilter")
public class CardCategory extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "card_id", nullable = false)
    private Long cardId;

    @Column(name = "category_id", nullable = false)
    private Long categoryId;

    private CardCategory(Long cardId, Long categoryId) {
        this.cardId = cardId;
        this.categoryId = categoryId;
    }

    public static CardCategory create(Long cardId, Long categoryId) {
        if (cardId == null) {
            throw new CategoryBusinessException(CategoryErrorCode.CARD_ID_IS_REQUIRED);
        }
        if (categoryId == null) {
            throw new CategoryBusinessException(CategoryErrorCode.CATEGORY_ID_IS_REQUIRED);
        }
        return new CardCategory(cardId, categoryId);
    }
}
