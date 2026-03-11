package com.dekk.deck.infrastructure;

import com.dekk.deck.domain.model.Deck;
import com.dekk.deck.domain.repository.DeckRepository;
import com.dekk.deck.infrastructure.jpa.DeckJpaRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

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

    @Override
    public Optional<Deck> findByIdAndUserId(Long id, Long userId) {
        return deckJpaRepository.findByIdAndUserId(id, userId);
    }

    @Override
    public void delete(Deck deck) {
        deckJpaRepository.delete(deck);
    }

    @Override
    public List<Deck> findAllByUserIdAndIsDefaultFalseOrderByCreatedAtDesc(Long userId) {
        return deckJpaRepository.findAllByUserIdAndIsDefaultFalseOrderByCreatedAtDesc(userId);
    }
}
