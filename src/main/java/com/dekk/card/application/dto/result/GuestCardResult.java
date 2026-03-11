package com.dekk.card.application.dto.result;

import com.dekk.card.domain.model.Card;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public record GuestCardResult(Long cardId, String cardImageUrl, Integer height, Integer weight, List<String> tags) {
    public static GuestCardResult from(Card card) {
        return new GuestCardResult(
                card.getId(),
                card.getCardImage().getImageUrl(),
                card.getHeight(),
                card.getWeight(),
                parseTags(card.getTags()));
    }

    private static List<String> parseTags(String tags) {
        if (tags == null || tags.isBlank()) {
            return Collections.emptyList();
        }
        return Arrays.stream(tags.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();
    }
}
