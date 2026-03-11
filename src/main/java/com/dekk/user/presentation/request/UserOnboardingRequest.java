package com.dekk.user.presentation.request;

import com.dekk.user.application.command.UserOnboardingCommand;
import com.dekk.user.domain.model.enums.Gender;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserOnboardingRequest(
        @NotBlank(message = "닉네임은 필수입니다.")
        @Size(min = 2, max = 10, message = "닉네임은 2자 이상 10자 이내여야 합니다.")
        @Pattern(regexp = "^[a-zA-Z0-9가-힣_]+$", message = "닉네임은 한글, 영문, 숫자, 언더바(_)만 허용됩니다.")
        String nickname,

        @NotNull(message = "키는 필수 입력값입니다.")
        @Min(value = 100, message = "키는 100cm 이상이어야 합니다.")
        @Max(value = 220, message = "키는 220cm 이하이어야 합니다.")
        Integer height,

        @NotNull(message = "몸무게는 필수 입력값입니다.")
        @Min(value = 30, message = "몸무게는 30kg 이상이어야 합니다.")
        @Max(value = 150, message = "몸무게는 150kg 이하이어야 합니다.")
        Integer weight,

        @NotNull(message = "성별은 필수 입력값입니다.") Gender gender) {
    public UserOnboardingCommand toCommand() {
        return new UserOnboardingCommand(nickname, height, weight, gender);
    }
}
