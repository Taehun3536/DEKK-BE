package com.dekk.deck.infrastructure;

import com.dekk.deck.domain.model.DeckCard;
import com.dekk.deck.domain.repository.DeckCardRepository;
import com.dekk.deck.infrastructure.jpa.DeckCardCountProjection;
import com.dekk.deck.infrastructure.jpa.DeckCardJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Collections.emptyMap;

@Repository
@RequiredArgsConstructor
public class DeckCardRepositoryImpl implements DeckCardRepository {

    private final DeckCardJpaRepository deckCardJpaRepository;

    @Override
    public DeckCard save(DeckCard deckCard) {
        return deckCardJpaRepository.save(deckCard);
    }

    @Override
    public boolean existsByDeckIdAndCardId(Long deckId, Long cardId) {
        return deckCardJpaRepository.existsByDeckIdAndCardId(deckId, cardId);
    }

    @Override
    public Page<DeckCard> findAllByDeckId(Long deckId, Pageable pageable) {
        return deckCardJpaRepository.findAllByDeckId(deckId, pageable);
    }

    @Override
    public Optional<DeckCard> findByDeckIdAndCardId(Long deckId, Long cardId) {
        return deckCardJpaRepository.findByDeckIdAndCardId(deckId, cardId);
    }

    @Override
    public void delete(DeckCard deckCard) {
        deckCardJpaRepository.delete(deckCard);
    }

    @Override
    public void deleteAllByDeckId(Long deckId) {
        deckCardJpaRepository.deleteAllByDeckId(deckId);
    }

    @Override
    public Map<Long, Long> countCardsByDeckIds(List<Long> deckIds) {
        if (deckIds == null || deckIds.isEmpty()) {
            return emptyMap();
        }

        return deckCardJpaRepository.countCardsByDeckIds(deckIds).stream()
            .collect(Collectors.toMap(
                DeckCardCountProjection::getDeckId,
                DeckCardCountProjection::getCardCount
            ));
    }

    @Override
    public long countByDeckId(Long deckId) {
        return deckCardJpaRepository.countByDeckId(deckId);
    }

    @Override
    public List<DeckCard> findAllByDeckIdIn(List<Long> deckIds) {
        if (deckIds == null || deckIds.isEmpty()) {
            return List.of();
        }
        return deckCardJpaRepository.findAllByDeckIdIn(deckIds);
    }

    @Override
    public List<DeckCard> findTopCardsByDeckIdsIn(List<Long> deckIds, int limit) {
        if (deckIds == null || deckIds.isEmpty()) {
            return List.of();
        }
        return deckCardJpaRepository.findTopCardsByDeckIdsIn(deckIds, limit);
    }
}
