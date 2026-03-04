package com.dekk.card.domain.model;

import com.dekk.card.application.command.CardCreateCommand;
import com.dekk.card.domain.exception.CardBusinessException;
import com.dekk.card.domain.exception.CardErrorCode;
import com.dekk.card.domain.model.enums.Platform;
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

    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Platform platform;

    private Integer height;

    private Integer weight;

    private Card(
            CardImage cardImage,
            String tags,
            String originId,
            Boolean isActive,
            Platform platform,
            Integer height,
            Integer weight
    ) {
        this.cardImage = cardImage;
        this.tags = tags;
        this.originId = originId;
        this.isActive = isActive;
        this.platform = platform;
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
                command.isActive(),
                command.platform(),
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
}
