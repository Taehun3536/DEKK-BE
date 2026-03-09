package com.dekk.deck.application.dto.result;

public record CustomDeckResult(
    Long deckId,
    String name,
    Long cardCount
) {
    public static CustomDeckResult of(Long deckId, String name, Long cardCount) {
        return new CustomDeckResult(deckId, name, cardCount);
    }
}
