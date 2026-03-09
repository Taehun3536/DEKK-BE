package com.dekk.deck.domain.model;

import com.dekk.common.entity.BaseTimeEntity;
import com.dekk.deck.domain.exception.DeckBusinessException;
import com.dekk.deck.domain.exception.DeckErrorCode;
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
@Table(name = "decks")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE decks SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@Filter(name = "deletedFilter")
public class Deck extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private boolean isDefault;

    private Deck(Long userId, String name, boolean isDefault) {
        this.userId = userId;
        this.name = name;
        this.isDefault = isDefault;
    }

    public static Deck createDefault(Long userId) {
        String defaultName = "나의 기본 보관함";
        validateEmpty(userId, defaultName);
        return new Deck(userId, defaultName, true);
    }

    public static Deck createCustom(Long userId, String name) {
        validateEmpty(userId, name);
        validateCustomNameLength(name);
        return new Deck(userId, name, false);
    }

    public void deleteCustom() {
        validateCustomModifiable();
    }

    private static void validateEmpty(Long userId, String name) {
        if (userId == null) {
            throw new DeckBusinessException(DeckErrorCode.USER_ID_IS_REQUIRED_TO_CREATE);
        }
        if (name == null || name.isBlank()) {
            throw new DeckBusinessException(DeckErrorCode.DECK_NAME_IS_REQUIRED_TO_CREATE);
        }
    }

    private static void validateCustomNameLength(String name) {
        if (name.length() < 1 || name.length() > 15) {
            throw new DeckBusinessException(DeckErrorCode.CUSTOM_DECK_NAME_LENGTH_INVALID);
        }
    }

    public void updateCustomName(String newName) {
        validateCustomModifiable();
        validateEmpty(this.userId, newName);
        validateCustomNameLength(newName);
        this.name = newName;
    }

    private void validateCustomModifiable() {
        if (this.isDefault) {
            throw new DeckBusinessException(DeckErrorCode.DEFAULT_DECK_CANNOT_BE_MODIFIED);
        }
    }
}
