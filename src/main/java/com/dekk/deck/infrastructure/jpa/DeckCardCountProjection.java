package com.dekk.deck.infrastructure.jpa;

public interface DeckCardCountProjection {
    Long getDeckId();

    Long getCardCount();
}
