package com.dekk.deck.presentation.controller;

import com.dekk.common.response.ApiResponse;
import com.dekk.deck.presentation.request.CustomDeckCreateRequest;
import com.dekk.deck.presentation.request.CustomDeckUpdateRequest;
import com.dekk.security.oauth2.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "커스텀 보관함 관리 API", description = "커스텀 보관함 생성, 수정, 삭제 및 내부 카드 관리 API")
public interface CustomDeckCommandApi {

    @Operation(summary = "커스텀 보관함 생성", description = "새로운 커스텀 보관함을 생성합니다. (최대 8개)")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공 (SD20005)"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "이름 길이 오류(ED40005) / 생성 개수 8개 초과(ED40006)",
            content = @Content(schema = @Schema(implementation = com.dekk.common.error.ErrorResponse.class))
        )
    })
    ResponseEntity<ApiResponse<Void>> createCustomDeck(
        @Parameter(hidden = true) CustomUserDetails userDetails,
        @RequestBody(description = "생성할 커스텀 보관함 정보") CustomDeckCreateRequest request
    );

    @Operation(summary = "커스텀 보관함 이름 수정", description = "커스텀 보관함의 이름을 수정합니다.")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공 (SD20006)"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "기본 보관함 수정 시도(ED40007) / 이름 길이 오류(ED40005)",
            content = @Content(schema = @Schema(implementation = com.dekk.common.error.ErrorResponse.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "커스텀 보관함을 찾을 수 없습니다(ED40403)",
            content = @Content(schema = @Schema(implementation = com.dekk.common.error.ErrorResponse.class))
        )
    })
    ResponseEntity<ApiResponse<Void>> updateCustomDeckName(
        @Parameter(hidden = true) CustomUserDetails userDetails,
        @Parameter(description = "수정할 커스텀 보관함 ID", in = ParameterIn.PATH) Long customDeckId,
        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "수정할 보관함 이름 정보") CustomDeckUpdateRequest request
    );

    @Operation(summary = "커스텀 보관함 삭제", description = "커스텀 보관함을 삭제합니다. 내부의 카드 정보도 함께 삭제 처리됩니다.")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공 (SD20007)"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "기본 보관함 삭제 시도(ED40007)",
            content = @Content(schema = @Schema(implementation = com.dekk.common.error.ErrorResponse.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "보관함을 찾을 수 없습니다(ED40403)",
            content = @Content(schema = @Schema(implementation = com.dekk.common.error.ErrorResponse.class))
        )
    })
    ResponseEntity<ApiResponse<Void>> deleteCustomDeck(
        @Parameter(hidden = true) CustomUserDetails userDetails,
        @Parameter(description = "삭제할 커스텀 보관함 ID", in = ParameterIn.PATH) Long customDeckId
    );

    @Operation(summary = "커스텀 보관함에 카드 저장", description = "커스텀 보관함에 특정 카드를 저장합니다. (최대 50장)")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공 (SD20009)"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "보관함 카드 개수 50개 초과(ED40008)",
            content = @Content(schema = @Schema(implementation = com.dekk.common.error.ErrorResponse.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "커스텀 보관함을 찾을 수 없습니다(ED40403)",
            content = @Content(schema = @Schema(implementation = com.dekk.common.error.ErrorResponse.class))
        )
    })
    ResponseEntity<ApiResponse<Void>> saveCardToCustomDeck(
        @Parameter(hidden = true) CustomUserDetails userDetails,
        @Parameter(description = "저장할 커스텀 보관함 ID", in = ParameterIn.PATH) Long customDeckId,
        @Parameter(description = "저장할 카드 ID", in = ParameterIn.PATH) Long cardId
    );

    @Operation(summary = "커스텀 보관함 내 카드 삭제", description = "커스텀 보관함에서 특정 카드를 삭제(Soft Delete)합니다.")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공 (SD20010)"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "커스텀 보관함 또는 덱 내부의 카드를 찾을 수 없습니다(ED40403, ED40402)",
            content = @Content(schema = @Schema(implementation = com.dekk.common.error.ErrorResponse.class))
        )
    })
    ResponseEntity<ApiResponse<Void>> removeCardFromCustomDeck(
        @Parameter(hidden = true) CustomUserDetails userDetails,
        @Parameter(description = "커스텀 보관함 ID", in = ParameterIn.PATH) Long customDeckId,
        @Parameter(description = "삭제할 카드 ID", in = ParameterIn.PATH) Long cardId
    );
}
