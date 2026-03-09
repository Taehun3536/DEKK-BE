package com.dekk.card.domain.model;

import com.dekk.card.application.command.CardCreateCommand;
import com.dekk.card.domain.exception.CardBusinessException;
import com.dekk.card.domain.exception.CardErrorCode;
import com.dekk.card.domain.model.enums.CardStatus;
import com.dekk.card.domain.model.enums.Platform;
import com.dekk.card.domain.model.enums.TargetGender;
import com.dekk.common.entity.BaseTimeEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Table(name = "cards")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Card extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "card", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private CardImage cardImage;

    @OneToMany(mappedBy = "card", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CardProduct> cardProducts = new ArrayList<>();

    @Column(name = "tags")
    private String tags;

    @Column(name = "origin_id", nullable = false, updatable = false)
    private String originId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private CardStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Platform platform;

    @Enumerated(EnumType.STRING)
    @Column(name = "target_gender")
    private TargetGender targetGender;

    private Integer height;

    private Integer weight;

    private Card(
            CardImage cardImage,
            String tags,
            String originId,
            Platform platform,
            TargetGender targetGender,
            Integer height,
            Integer weight
    ) {
        this.cardImage = cardImage;
        this.tags = tags;
        this.originId = originId;
        this.status = CardStatus.PENDING;
        this.platform = platform;
        this.targetGender = targetGender;
        this.height = height;
        this.weight = weight;
    }

    public static Card create(CardCreateCommand command) {
        if (command.originId() == null) {
            throw new CardBusinessException(CardErrorCode.CARD_ORIGIN_ID_IS_REQUIRED_TO_CREATE);
        }

        CardImage cardImage = CardImage.create(command.cardImage());

        Card card = new Card(
                cardImage,
                command.tags(),
                command.originId(),
                command.platform(),
                command.targetGender(),
                command.height(),
                command.weight()
        );

        cardImage.setCard(card);
        command.productCreateCommands().stream()
                .map(Product::create)
                .map(product -> CardProduct.create(card, product))
                .forEach(card.cardProducts::add);
        return card;
    }

    public void approve() {
        validateStatusChangeable();
        this.status = CardStatus.APPROVED;
    }

    public void reject() {
        validateStatusChangeable();
        this.status = CardStatus.REJECTED;
    }

    private void validateStatusChangeable() {
        if (!this.status.canChangeStatus()) {
            throw new CardBusinessException(CardErrorCode.CANNOT_CHANGE_STATUS_OF_DELETE_REQUESTED);
        }
    }
}
