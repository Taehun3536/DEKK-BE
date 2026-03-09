package com.dekk.deck.domain.repository;

import com.dekk.deck.domain.model.DeckCard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface DeckCardRepository {
    DeckCard save(DeckCard deckCard);

    boolean existsByDeckIdAndCardId(Long deckId, Long cardId);

    Page<DeckCard> findAllByDeckId(Long deckId, Pageable pageable);

    Optional<DeckCard> findByDeckIdAndCardId(Long deckId, Long cardId);

    void delete(DeckCard deckCard);

    void deleteAllByDeckId(Long deckId);

    Map<Long, Long> countCardsByDeckIds(List<Long> deckIds);

    long countByDeckId(Long deckId);

    List<DeckCard> findAllByDeckIdIn(List<Long> deckIds);

    List<DeckCard> findTopCardsByDeckIdsIn(List<Long> deckIds, int limit);
}
