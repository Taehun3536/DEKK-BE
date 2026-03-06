package com.dekk.deck.domain.repository;

import com.dekk.deck.domain.model.Deck;
import java.util.Optional;

public interface DeckRepository {
    Deck save(Deck deck);
    Optional<Deck> findByUserIdAndIsDefaultTrue(Long userId);
    long countByUserIdAndIsDefaultFalse(Long userId);
}
