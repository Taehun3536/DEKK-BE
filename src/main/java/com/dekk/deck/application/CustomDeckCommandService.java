package com.dekk.deck.application;

import com.dekk.deck.application.dto.command.CustomDeckCreateCommand;
import com.dekk.deck.domain.exception.DeckBusinessException;
import com.dekk.deck.domain.exception.DeckErrorCode;
import com.dekk.deck.domain.model.Deck;
import com.dekk.deck.domain.repository.DeckRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CustomDeckCommandService {

    private static final int MAX_CUSTOM_DECK_COUNT = 8;

    private final DeckRepository deckRepository;

    public void createCustomDeck(Long userId, CustomDeckCreateCommand command) {
        long currentCount = deckRepository.countByUserIdAndIsDefaultFalse(userId);

        if (currentCount >= MAX_CUSTOM_DECK_COUNT) {
            throw new DeckBusinessException(DeckErrorCode.CUSTOM_DECK_LIMIT_EXCEEDED);
        }

        Deck customDeck = Deck.createCustom(userId, command.name());
        deckRepository.save(customDeck);
    }
}
