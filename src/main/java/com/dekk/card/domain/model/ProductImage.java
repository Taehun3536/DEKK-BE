package com.dekk.card.domain.model;

import com.dekk.card.application.command.ProductImageCreateCommand;
import com.dekk.card.domain.exception.CardBusinessException;
import com.dekk.card.domain.exception.CardErrorCode;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Table(name = "product_images")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "origin_url", nullable = false, columnDefinition = "text")
    private String originUrl;

    @Column(name = "image_url", columnDefinition = "text")
    private String imageUrl;

    @Column(name = "is_uploaded", nullable = false)
    private boolean isUploaded;

    private ProductImage(String originUrl, String imageUrl, boolean isUploaded) {
        this.originUrl = originUrl;
        this.imageUrl = imageUrl;
        this.isUploaded = isUploaded;
    }

    protected static ProductImage create(ProductImageCreateCommand command) {
        if (command.originUrl() == null) {
            throw new CardBusinessException(CardErrorCode.PRODUCT_ORIGIN_URL_IS_REQUIRED_TO_CREATE);
        }
        return new ProductImage(command.originUrl(), command.imageUrl(), command.isUploaded());
    }

    protected void setProduct(Product product) {
        this.product = product;
    }
}
