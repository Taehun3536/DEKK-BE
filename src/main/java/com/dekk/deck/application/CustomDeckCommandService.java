package com.dekk.deck.application;

import com.dekk.deck.application.dto.command.CustomDeckCreateCommand;
import com.dekk.deck.application.dto.command.CustomDeckUpdateCommand;
import com.dekk.deck.domain.exception.DeckBusinessException;
import com.dekk.deck.domain.exception.DeckErrorCode;
import com.dekk.deck.domain.model.Deck;
import com.dekk.deck.domain.repository.DeckCardRepository;
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

    private final DeckCardRepository deckCardRepository;

    public void createCustomDeck(Long userId, CustomDeckCreateCommand command) {
        long currentCount = deckRepository.countByUserIdAndIsDefaultFalse(userId);

        if (currentCount >= MAX_CUSTOM_DECK_COUNT) {
            throw new DeckBusinessException(DeckErrorCode.CUSTOM_DECK_LIMIT_EXCEEDED);
        }

        Deck customDeck = Deck.createCustom(userId, command.name());
        deckRepository.save(customDeck);
    }

    public void updateCustomDeckName(Long userId, Long deckId, CustomDeckUpdateCommand command) {
        Deck deck = getDeckByUserId(deckId, userId);

        deck.updateCustomName(command.name());
    }

    private Deck getDeckByUserId(Long deckId, Long userId) {
        return deckRepository.findByIdAndUserId(deckId, userId)
            .orElseThrow(() -> new DeckBusinessException(DeckErrorCode.CUSTOM_DECK_NOT_FOUND));
    }

    public void deleteCustomDeck(Long userId, Long deckId) {
        Deck deck = getDeckByUserId(deckId, userId);

        deck.deleteCustom();

        deckCardRepository.deleteAllByDeckId(deck.getId());

        deckRepository.delete(deck);
    }
}
