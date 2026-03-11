package com.dekk.admin.domain.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AdminRole {
    SUPER_ADMIN("SUPER_ADMIN", "'슈퍼 관리자 권한"),
    ADMIN("ADMIN", "관리자 권한");

    private final String key;
    private final String title;
}
