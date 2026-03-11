package com.dekk.user.presentation.request;

import com.dekk.user.application.command.UserProfileUpdateCommand;
import com.dekk.user.domain.model.enums.Gender;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public record UserProfileUpdateRequest(
        @Size(min = 2, max = 10, message = "닉네임은 2자 이상 10자 이내여야 합니다.")
        String nickname,

        @Min(value = 100, message = "키는 100cm 이상이어야 합니다.") @Max(value = 220, message = "키는 220cm 이하이어야 합니다.")
        Integer height,

        @Min(value = 30, message = "몸무게는 30kg 이상이어야 합니다.") @Max(value = 150, message = "몸무게는 150kg 이하이어야 합니다.")
        Integer weight,

        Gender gender) {
    public UserProfileUpdateCommand toCommand() {
        return new UserProfileUpdateCommand(nickname, height, weight, gender);
    }
}
