package com.dekk.deck.application.dto.result;

public record CustomDeckResult(Long deckId, String name, Long cardCount, String imageUrl) {
    public static CustomDeckResult of(Long deckId, String name, Long cardCount, String imageUrl) {
        return new CustomDeckResult(deckId, name, cardCount, imageUrl);
    }
}
