package com.dekk.deck.presentation.controller;

import com.dekk.common.response.ApiResponse;
import com.dekk.common.response.PageResponse;
import com.dekk.deck.application.CustomDeckQueryService;
import com.dekk.deck.application.dto.result.CustomDeckResult;
import com.dekk.deck.application.dto.result.MyDeckCardResult;
import com.dekk.deck.presentation.response.DeckResultCode;
import com.dekk.security.oauth2.CustomUserDetails;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/w/v1/decks/custom")
@RequiredArgsConstructor
public class CustomDeckQueryController implements CustomDeckQueryApi {

    private final CustomDeckQueryService customDeckQueryService;

    @Override
    @GetMapping
    public ResponseEntity<ApiResponse<List<CustomDeckResult>>> getMyCustomDecks(
        @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        List<CustomDeckResult> result = customDeckQueryService.getMyCustomDecks(userDetails.getId());

        return ResponseEntity.ok(ApiResponse.of(
            DeckResultCode.CUSTOM_DECK_LIST_SUCCESS,
            result
        ));
    }

    @Override
    @GetMapping("/{customDeckId}/cards")
    public ResponseEntity<ApiResponse<PageResponse<MyDeckCardResult>>> getCustomDeckCards(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @PathVariable("customDeckId") Long customDeckId,
        @ParameterObject Pageable pageable
    ) {
        Page<MyDeckCardResult> result = customDeckQueryService.getCustomDeckCards(userDetails.getId(), customDeckId, pageable);

        return ResponseEntity.ok(ApiResponse.of(
            DeckResultCode.CUSTOM_DECK_CARD_LIST_SUCCESS,
            PageResponse.from(result)
        ));
    }
}
