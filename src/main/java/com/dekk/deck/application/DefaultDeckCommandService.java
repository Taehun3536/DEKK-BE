package com.dekk.deck.application;

import com.dekk.deck.domain.model.Deck;
import com.dekk.deck.domain.repository.DeckRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class DefaultDeckCommandService {

    private final DeckRepository deckRepository;

    public void createDefaultDeck(Long userId) {
        if (deckRepository.findByUserIdAndIsDefaultTrue(userId).isPresent()) {
            return;
        }

        Deck defaultDeck = Deck.createDefault(userId);
        deckRepository.save(defaultDeck);
    }
}
