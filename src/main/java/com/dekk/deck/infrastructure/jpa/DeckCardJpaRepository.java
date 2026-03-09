package com.dekk.deck.infrastructure.jpa;

import com.dekk.deck.domain.model.DeckCard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DeckCardJpaRepository extends JpaRepository<DeckCard, Long> {
    boolean existsByDeckIdAndCardId(Long deckId, Long cardId);

    Page<DeckCard> findAllByDeckId(Long deckId, Pageable pageable);

    Optional<DeckCard> findByDeckIdAndCardId(Long deckId, Long CardId);

    void deleteAllByDeckId(Long deckId);

    @Query("""
         SELECT d.deckId AS deckId, COUNT(d) AS cardCount
         FROM DeckCard d
         WHERE d.deckId IN :deckIds
         GROUP BY d.deckId
        """)
    List<DeckCardCountProjection> countCardsByDeckIds(@Param("deckIds") List<Long> deckIds);

    long countByDeckId(Long deckId);

    List<DeckCard> findAllByDeckIdIn(List<Long> deckIds);

    @Query(value = """
        SELECT * FROM (
            SELECT dc.*, ROW_NUMBER() OVER(PARTITION BY dc.deck_id ORDER BY dc.created_at DESC) as rn 
            FROM deck_cards dc 
            WHERE dc.deck_id IN (:deckIds) AND dc.deleted_at IS NULL
        ) sub 
        WHERE sub.rn <= :limit
        """, nativeQuery = true)
    List<DeckCard> findTopCardsByDeckIdsIn(
        @Param("deckIds") List<Long> deckIds,
        @Param("limit") int limit
    );
}
