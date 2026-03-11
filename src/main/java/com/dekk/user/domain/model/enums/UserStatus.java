package com.dekk.user.domain.model.enums;

public enum UserStatus {
    PENDING, // 소셜 로그인 완료, 온보딩 미완료
    ACTIVE, // 정상 사용자
    INACTIVE, // 비활성 (장기 미접속 등)
    DELETED // 탈퇴
}
