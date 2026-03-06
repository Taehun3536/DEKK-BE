package com.dekk.deck.infrastructure;

import com.dekk.deck.domain.model.Deck;
import com.dekk.deck.domain.repository.DeckRepository;
import com.dekk.deck.infrastructure.jpa.DeckJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class DeckRepositoryImpl implements DeckRepository {

    private final DeckJpaRepository deckJpaRepository;

    @Override
    public Deck save(Deck deck) {
        return deckJpaRepository.save(deck);
    }

    @Override
    public Optional<Deck> findByUserIdAndIsDefaultTrue(Long userId) {
        return deckJpaRepository.findByUserIdAndIsDefaultTrue(userId);
    }

    @Override
    public long countByUserIdAndIsDefaultFalse(Long userId) {
        return deckJpaRepository.countByUserIdAndIsDefaultFalse(userId);
    }
}
