package com.dekk.deck.presentation.controller;

import com.dekk.common.response.ApiResponse;
import com.dekk.deck.application.CustomDeckCommandService;
import com.dekk.deck.application.DeckCardCommandService;
import com.dekk.deck.presentation.request.CustomDeckCreateRequest;
import com.dekk.deck.presentation.request.CustomDeckUpdateRequest;
import com.dekk.deck.presentation.response.DeckResultCode;
import com.dekk.security.oauth2.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/w/v1/decks/custom")
@RequiredArgsConstructor
public class CustomDeckCommandController implements CustomDeckCommandApi {

    private final CustomDeckCommandService customDeckCommandService;
    private final DeckCardCommandService deckCardCommandService;

    @Override
    @PostMapping
    public ResponseEntity<ApiResponse<Void>> createCustomDeck(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @Valid @RequestBody CustomDeckCreateRequest request
    ) {
        customDeckCommandService.createCustomDeck(userDetails.getId(), request.toCommand());

        return ResponseEntity.ok(ApiResponse.from(DeckResultCode.CUSTOM_DECK_CREATE_SUCCESS));
    }

    @Override
    @PatchMapping("/{customDeckId}")
    public ResponseEntity<ApiResponse<Void>> updateCustomDeckName(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @PathVariable("customDeckId") Long customDeckId,
        @Valid @RequestBody CustomDeckUpdateRequest request
    ) {
        customDeckCommandService.updateCustomDeckName(userDetails.getId(), customDeckId, request.toCommand());

        return ResponseEntity.ok(ApiResponse.from(DeckResultCode.CUSTOM_DECK_UPDATE_SUCCESS));
    }

    @Override
    @DeleteMapping("/{customDeckId}")
    public ResponseEntity<ApiResponse<Void>> deleteCustomDeck(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @PathVariable("customDeckId") Long customDeckId
    ) {
        customDeckCommandService.deleteCustomDeck(userDetails.getId(), customDeckId);

        return ResponseEntity.ok(ApiResponse.from(DeckResultCode.CUSTOM_DECK_DELETE_SUCCESS));
    }

    @Override
    @PostMapping("/{customDeckId}/cards/{cardId}")
    public ResponseEntity<ApiResponse<Void>> saveCardToCustomDeck(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @PathVariable("customDeckId") Long customDeckId,
        @PathVariable("cardId") Long cardId
    ) {
        deckCardCommandService.saveToCustomDeck(userDetails.getId(), customDeckId, cardId);
        return ResponseEntity.ok(ApiResponse.from(DeckResultCode.CUSTOM_DECK_CARD_SAVE_SUCCESS));
    }

    @Override
    @DeleteMapping("/{customDeckId}/cards/{cardId}")
    public ResponseEntity<ApiResponse<Void>> removeCardFromCustomDeck(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @PathVariable("customDeckId") Long customDeckId,
        @PathVariable("cardId") Long cardId
    ) {
        deckCardCommandService.removeFromCustomDeck(userDetails.getId(), customDeckId, cardId);
        return ResponseEntity.ok(ApiResponse.from(DeckResultCode.CUSTOM_DECK_CARD_DELETE_SUCCESS));
    }
}
