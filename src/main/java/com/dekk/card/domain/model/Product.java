package com.dekk.card.domain.model;

import com.dekk.card.application.command.ProductCreateCommand;
import com.dekk.card.domain.exception.CardBusinessException;
import com.dekk.card.domain.exception.CardErrorCode;
import com.dekk.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Table(name = "products")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private ProductImage productImage;

    @Column(nullable = false)
    private String brand;

    @Column(nullable = false)
    private String name;

    private Integer price;

    @Column(name = "origin_id", nullable = false)
    private String originId;

    private String option;

    @Column(name = "is_similar", nullable = false)
    private boolean isSimilar;

    @Column(name = "product_url", columnDefinition = "text")
    private String productUrl;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    private Product(
            ProductImage productImage,
            String brand,
            String name,
            Integer price,
            String originId,
            String option,
            Boolean isSimilar,
            String productUrl,
            boolean isActive) {
        this.productImage = productImage;
        this.brand = brand;
        this.name = name;
        this.price = price;
        this.originId = originId;
        this.option = option;
        this.isSimilar = isSimilar;
        this.productUrl = productUrl;
        this.isActive = isActive;
    }

    public static Product create(ProductCreateCommand command) {
        ProductImage productImage = ProductImage.create(command.productImage());

        if (command.name() == null) {
            throw new CardBusinessException(CardErrorCode.PRODUCT_NAME_IS_REQUIRED_TO_CREATE);
        }

        if (command.originId() == null) {
            throw new CardBusinessException(CardErrorCode.PRODUCT_EXTERNAL_ID_IS_REQUIRED_TO_CREATE);
        }

        Product product = new Product(
                productImage,
                command.brand(),
                command.name(),
                command.price(),
                command.originId(),
                command.option(),
                command.isSimilar(),
                command.productUrl(),
                command.isActive());

        productImage.setProduct(product);
        return product;
    }
}
