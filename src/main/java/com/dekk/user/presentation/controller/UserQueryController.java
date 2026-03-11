package com.dekk.user.presentation.controller;

import com.dekk.common.response.ApiResponse;
import com.dekk.security.oauth2.CustomUserDetails;
import com.dekk.user.application.UserQueryService;
import com.dekk.user.application.dto.result.UserInfoResult;
import com.dekk.user.presentation.response.UserInfoResponse;
import com.dekk.user.presentation.response.UserResultCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/w/v1/users")
@RequiredArgsConstructor
public class UserQueryController implements UserQueryApi {

    private final UserQueryService userQueryService;

    @Override
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserInfoResponse>> getMyInfo(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        UserInfoResult result = userQueryService.getMyInfo(userDetails.getId());
        UserInfoResponse response = UserInfoResponse.from(result);

        return ResponseEntity.ok(ApiResponse.of(UserResultCode.GET_MY_INFO_SUCCESS, response));
    }
}
