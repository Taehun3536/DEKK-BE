package com.dekk.deck.presentation.controller;

import com.dekk.common.response.ApiResponse;
import com.dekk.deck.application.CustomDeckCommandService;
import com.dekk.deck.presentation.request.CustomDeckCreateRequest;
import com.dekk.deck.presentation.response.DeckResultCode;
import com.dekk.security.oauth2.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/w/v1/decks/custom")
@RequiredArgsConstructor
public class CustomDeckCommandController implements CustomDeckCommandApi {

    private final CustomDeckCommandService customDeckCommandService;

    @Override
    @PostMapping
    public ResponseEntity<ApiResponse<Void>> createCustomDeck(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @Valid @RequestBody CustomDeckCreateRequest request
    ) {
        customDeckCommandService.createCustomDeck(userDetails.getId(), request.toCommand());

        return ResponseEntity.ok(ApiResponse.from(DeckResultCode.CUSTOM_DECK_CREATE_SUCCESS));
    }
}
