package com.dekk.deck.presentation.controller;

import com.dekk.common.response.ApiResponse;
import com.dekk.deck.application.DeckQueryService;
import com.dekk.deck.application.dto.result.DeckResult;
import com.dekk.deck.presentation.response.DeckResultCode;
import com.dekk.security.oauth2.CustomUserDetails;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/w/v1/decks")
@RequiredArgsConstructor
public class DeckQueryController implements DeckQueryApi {

    private final DeckQueryService deckQueryService;

    @Override
    @GetMapping
    public ResponseEntity<ApiResponse<List<DeckResult>>> getDecks(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        List<DeckResult> result = deckQueryService.getDecks(userDetails.getId());

        return ResponseEntity.ok(ApiResponse.of(DeckResultCode.DECK_LIST_SUCCESS, result));
    }
}
