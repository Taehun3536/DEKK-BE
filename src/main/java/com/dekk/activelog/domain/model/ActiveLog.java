package com.dekk.activelog.domain.model;

import com.dekk.activelog.domain.exception.ActiveLogBusinessException;
import com.dekk.activelog.domain.exception.ActiveLogErrorCode;
import com.dekk.common.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.SQLDelete;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        name = "active_logs",
        indexes = {@Index(name = "idx_user_card", columnList = "user_id, card_id")},
        uniqueConstraints = {
            @UniqueConstraint(
                    name = "uk_active_logs_user_card",
                    columnNames = {"user_id", "card_id"})
        })
@SQLDelete(sql = "UPDATE active_logs SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@Filter(name = "deletedFilter")
public class ActiveLog extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "card_id", nullable = false)
    private Long cardId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SwipeType swipeType;

    private ActiveLog(Long userId, Long cardId, SwipeType swipeType) {
        this.userId = userId;
        this.cardId = cardId;
        this.swipeType = swipeType;
    }

    public static ActiveLog create(Long userId, Long cardId, SwipeType swipeType) {
        if (userId == null) {
            throw new ActiveLogBusinessException(ActiveLogErrorCode.USER_ID_IS_REQUIRED_TO_CREATE);
        }
        if (cardId == null) {
            throw new ActiveLogBusinessException(ActiveLogErrorCode.CARD_ID_IS_REQUIRED_TO_CREATE);
        }
        if (swipeType == null) {
            throw new ActiveLogBusinessException(ActiveLogErrorCode.SWIPE_TYPE_IS_REQUIRED_TO_CREATE);
        }
        return new ActiveLog(userId, cardId, swipeType);
    }
}
