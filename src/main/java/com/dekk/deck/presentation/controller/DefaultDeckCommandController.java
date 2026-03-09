package com.dekk.deck.presentation.controller;

import com.dekk.common.response.ApiResponse;
import com.dekk.deck.application.DeckCardCommandService;
import com.dekk.deck.application.DefaultDeckCommandService;
import com.dekk.deck.presentation.response.DeckResultCode;
import com.dekk.security.oauth2.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/w/v1/decks/default")
@RequiredArgsConstructor
public class DefaultDeckCommandController implements DefaultDeckCommandApi {

    private final DefaultDeckCommandService deckCommandService;
    private final DeckCardCommandService deckCardCommandService;

    @Override
    @DeleteMapping("/cards/{cardId}")
    public ResponseEntity<ApiResponse<Void>> removeCardFromDefaultDeck(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @PathVariable("cardId") Long cardId
    ) {
        deckCardCommandService.removeFromDefaultDeck(userDetails.getId(), cardId);
        return ResponseEntity.ok(ApiResponse.from(DeckResultCode.DECK_CARD_DELETED_SUCCESS));
    }
}
