package com.dekk.deck.application.dto.result;

import java.util.Collections;
import java.util.List;

public record MyDeckCardResult(
    Long cardId,
    String cardImageUrl,
    Integer height,
    Integer weight,
    List<String> tag,
    List<ProductDetail> products
) {

    public static MyDeckCardResult empty(Long cardId) {
        return new MyDeckCardResult(
            cardId,
            null,
            null,
            null,
            Collections.emptyList(),
            Collections.emptyList()
        );
    }

    public record ProductDetail(
        String brand,
        String url,
        String name,
        String productsImageUrl
    ) {
    }
}
