package com.dekk.user.presentation.controller;

import com.dekk.common.response.ApiResponse;
import com.dekk.security.oauth2.CustomUserDetails;
import com.dekk.user.presentation.request.UserOnboardingRequest;
import com.dekk.user.presentation.request.UserProfileUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "사용자 정보 관리 API", description = "사용자 정보 및 상태 변경 API")
public interface UserCommandApi {

    @Operation(summary = "유저 온보딩", description = "소셜 로그인 후 최초 신체 정보 및 닉네임을 설정하고, 기본 보관함을 자동 생성합니다.")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "성공 (SU20001)"),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "400",
                        description = "잘못된 요청(EG001) / 이미 온보딩 됨(EU40001) / 닉네임 규칙 위반(EU40003) / 신체 정보 범위 오류(EU40006)",
                        content =
                                @Content(schema = @Schema(implementation = com.dekk.common.error.ErrorResponse.class))),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "404",
                        description = "사용자를 찾을 수 없음(EU40401)",
                        content =
                                @Content(schema = @Schema(implementation = com.dekk.common.error.ErrorResponse.class))),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "409",
                        description = "이미 사용 중인 닉네임(EU40901)",
                        content =
                                @Content(schema = @Schema(implementation = com.dekk.common.error.ErrorResponse.class)))
            })
    ResponseEntity<ApiResponse<Void>> onboardUser(
            @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody UserOnboardingRequest request);

    @Operation(summary = "프로필 업데이트", description = "닉네임, 키, 몸무게 등 사용자의 프로필 정보를 업데이트합니다.")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "성공 (SU20003)"),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "404",
                        description = "사용자를 찾을 수 없음(EU40401)",
                        content =
                                @Content(schema = @Schema(implementation = com.dekk.common.error.ErrorResponse.class))),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "409",
                        description = "이미 사용 중인 닉네임(EU40901)",
                        content =
                                @Content(schema = @Schema(implementation = com.dekk.common.error.ErrorResponse.class)))
            })
    ResponseEntity<ApiResponse<Void>> updateProfile(
            @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody UserProfileUpdateRequest request);

    @Operation(summary = "회원 탈퇴", description = "사용자 계정을 삭제하여 회원 탈퇴를 진행합니다.")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "성공 (SU20004)"),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "404",
                        description = "사용자를 찾을 수 없음(EU40401)",
                        content =
                                @Content(schema = @Schema(implementation = com.dekk.common.error.ErrorResponse.class)))
            })
    ResponseEntity<ApiResponse<Void>> deleteUser(
            @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails userDetails);
}
