package com.dekk.card.infrastructure.jpa;

import com.dekk.card.domain.model.Card;
import com.dekk.card.domain.model.enums.CardStatus;
import com.dekk.card.domain.model.enums.Platform;
import com.dekk.card.domain.model.enums.TargetGender;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CardJpaRepository extends JpaRepository<Card, Long> {
    boolean existsByPlatformAndOriginId(Platform platform, String originId);

    @Query(
            value = "SELECT c FROM Card c JOIN FETCH c.cardImage WHERE c.status = :status ORDER BY c.createdAt DESC",
            countQuery = "SELECT COUNT(c) FROM Card c WHERE c.status = :status")
    Page<Card> findCardsWithImageByStatus(@Param("status") CardStatus status, Pageable pageable);

    @Query(
            value = "SELECT c.id FROM Card c WHERE c.status = :status ORDER BY c.createdAt DESC",
            countQuery = "SELECT COUNT(c) FROM Card c WHERE c.status = :status")
    Page<Long> findCardIdsByStatus(@Param("status") CardStatus status, Pageable pageable);

    @Query("SELECT DISTINCT c FROM Card c " + "JOIN FETCH c.cardImage "
            + "LEFT JOIN FETCH c.cardProducts cp "
            + "LEFT JOIN FETCH cp.product p "
            + "LEFT JOIN FETCH p.productImage "
            + "WHERE c.id IN :ids ORDER BY c.createdAt DESC")
    List<Card> findAllByIdInWithProducts(@Param("ids") List<Long> ids);

    @Query("SELECT DISTINCT c FROM Card c " + "JOIN FETCH c.cardImage "
            + "JOIN FETCH c.cardProducts cp "
            + "LEFT JOIN FETCH cp.product p "
            + "LEFT JOIN FETCH p.productImage "
            + "WHERE c.status = 'APPROVED' "
            + "AND c.targetGender IN :genders "
            + "AND (c.height IS NULL OR c.height BETWEEN :minHeight AND :maxHeight) "
            + "AND (c.weight IS NULL OR c.weight BETWEEN :minWeight AND :maxWeight)")
    List<Card> findRecommendCandidates(
            @Param("genders") List<TargetGender> genders,
            @Param("minHeight") int minHeight,
            @Param("maxHeight") int maxHeight,
            @Param("minWeight") int minWeight,
            @Param("maxWeight") int maxWeight);
}
