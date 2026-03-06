package com.dekk.deck.infrastructure.jpa;

import com.dekk.deck.domain.model.Deck;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface DeckJpaRepository extends JpaRepository<Deck, Long> {
    Optional<Deck> findByUserIdAndIsDefaultTrue(Long userId);

    long countByUserIdAndIsDefaultFalse(Long userId);
}
