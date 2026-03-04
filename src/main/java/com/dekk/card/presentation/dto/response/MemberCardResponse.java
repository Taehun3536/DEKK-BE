package com.dekk.card.presentation.dto.response;

import com.dekk.card.application.dto.result.MemberCardResult;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "회원 카드 응답")
public record MemberCardResponse(
        @Schema(description = "카드 ID", example = "1")
        Long cardId,

        @Schema(description = "카드 이미지 URL", example = "https://example.com/card.jpg")
        String cardImageUrl,

        @Schema(description = "키(cm)", example = "175")
        Integer height,

        @Schema(description = "몸무게(kg)", example = "65")
        Integer weight,

        @Schema(description = "태그 목록", example = "[\"캐주얼\", \"스트릿\"]")
        List<String> tags,

        @Schema(description = "상품 목록")
        List<ProductDetailResponse> products
) {
    @Schema(description = "상품 상세 응답")
    public record ProductDetailResponse(
            @Schema(description = "상품 ID", example = "10")
            Long productId,

            @Schema(description = "브랜드", example = "Nike")
            String brand,

            @Schema(description = "상품명", example = "에어맥스 90")
            String name,

            @Schema(description = "상품 이미지 URL", example = "https://example.com/product.jpg")
            String productImageUrl,

            @Schema(description = "상품 URL", example = "https://example.com/product")
            String productUrl
    ) {
        public static ProductDetailResponse from(MemberCardResult.ProductDetail detail) {
            return new ProductDetailResponse(
                    detail.productId(),
                    detail.brand(),
                    detail.name(),
                    detail.productImageUrl(),
                    detail.productUrl()
            );
        }
    }

    public static MemberCardResponse from(MemberCardResult result) {
        return new MemberCardResponse(
                result.cardId(),
                result.cardImageUrl(),
                result.height(),
                result.weight(),
                result.tags(),
                result.products().stream()
                        .map(ProductDetailResponse::from)
                        .toList()
        );
    }
}
