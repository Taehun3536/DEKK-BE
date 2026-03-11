package com.dekk.card.application.dto.result;

import com.dekk.card.domain.model.Card;
import com.dekk.card.domain.model.CardProduct;
import com.dekk.card.domain.model.Product;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public record MemberCardResult(
        Long cardId,
        String cardImageUrl,
        Integer height,
        Integer weight,
        List<String> tags,
        List<ProductDetail> products) {
    public record ProductDetail(Long productId, String brand, String name, String productImageUrl, String productUrl) {
        public static ProductDetail from(Product product) {
            return new ProductDetail(
                    product.getId(),
                    product.getBrand(),
                    product.getName(),
                    product.getProductImage() != null
                            ? product.getProductImage().getImageUrl()
                            : null,
                    product.getProductUrl());
        }
    }

    public static MemberCardResult from(Card card) {
        List<ProductDetail> productDetails = card.getCardProducts().stream()
                .map(CardProduct::getProduct)
                .map(ProductDetail::from)
                .toList();

        return new MemberCardResult(
                card.getId(),
                card.getCardImage().getImageUrl(),
                card.getHeight(),
                card.getWeight(),
                parseTags(card.getTags()),
                productDetails);
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
