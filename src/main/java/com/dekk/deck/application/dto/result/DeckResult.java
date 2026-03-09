package com.dekk.deck.application.dto.result;

import com.dekk.deck.domain.model.enums.DeckType;

import java.util.List;

public record DeckResult(
    Long deckId,
    String name,
    DeckType type,
    Long cardCount,
    List<String> previewImageUrls
) {
}
