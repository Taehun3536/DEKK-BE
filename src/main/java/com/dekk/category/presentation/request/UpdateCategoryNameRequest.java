package com.dekk.category.presentation.request;

import com.dekk.category.application.command.UpdateCategoryNameCommand;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateCategoryNameRequest(
        @NotBlank(message = "카테고리 이름은 필수값입니다") @Size(max = 10, message = "카테고리 이름은 10자 이내여야 합니다")
        String name) {
    public UpdateCategoryNameCommand toCommand() {
        return new UpdateCategoryNameCommand(name);
    }
}
