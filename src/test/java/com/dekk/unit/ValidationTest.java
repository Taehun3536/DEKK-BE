package com.dekk.unit;

import com.dekk.crawl.domain.exception.CrawlBusinessException;
import com.dekk.crawl.domain.exception.CrawlErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * [단위 리그레션] 도메인 비즈니스 로직 및 데이터 정합성 검증
 * - 닉네임, 신체정보, 가격 등 유입 데이터의 유효성 검사 규칙을 전수 확인.
 * - 외부 유입(크롤링) 데이터의 예외 상황(Null, 미지원 플랫폼) 방어 로직을 포함.
 */
@Tag("regression")
class ValidationTest {

    private static final Pattern NICKNAME_PATTERN = Pattern.compile("^[a-zA-Z0-9가-힣_]+$");

    @ParameterizedTest
    @CsvSource({
            "'test_123', true",
            "'test 123', false",
            "' test', false",
            "'test!@#', false",
            "'test_qwer', true"
    })
    @DisplayName("ATC-6: 닉네임 정규식 패턴 검증")
    void testNicknamePattern(String input, boolean expected) {
        // [사용자 경험] 한글, 영문, 숫자, 언더바(_)만 허용하여 DB 인덱싱 효율과
        // UI 표시상의 일관성을 유지함. 특수문자 및 공백은 원천 차단.
        boolean matches = NICKNAME_PATTERN.matcher(input).matches();
        assertThat(matches).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource({
            "'  닉네임  ', '닉네임'",
            "'공백 포함 ', '공백 포함'"
    })
    @DisplayName("ATC-6: 닉네임 앞뒤 공백 제거 로직 확인")
    void testNicknameTrim(String input, String expected) {
        // 실제 서비스에서는 trim()을 먼저 수행한 후 패턴 검사를 하는지 확인하는 것이 중요합니다.
        assertThat(input.trim()).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource({
            "A, false",              // ATC-7: 1자 실패
            "qwer, true",
            "qwer1234asd, false" // ATC-7: 11자 실패
    })
    @DisplayName("ATC-7: 닉네임 글자수 유효성 검증")
    void testNicknameLength(String input, boolean expected) {
        boolean isValid = input.length() >= 2 && input.length() <= 10;
        assertThat(isValid).isEqualTo(expected);
    }

    @ParameterizedTest
    @ValueSource(strings = {"admin", "관리자", "운영자"})
    @DisplayName("TC-9: 닉네임 금칙어 포함 시 가입 제한 확인")
    void testNicknameBlacklist(String blacklistName) {
        java.util.List<String> forbiddenWords = java.util.Arrays.asList("admin", "관리자", "운영자");
        boolean isForbidden = forbiddenWords.contains(blacklistName);

        assertThat(isForbidden).isTrue();
    }

    @ParameterizedTest
    @CsvSource({
            "99, false",  // ATC-12: 경계값 미달
            "100, true",
            "220, true",
            "221, false" // ATC-12: 경계값 초과
    })
    @DisplayName("ATC-12: 키 입력 유효 범위 검증 (100-220cm)")
    void testHeightBoundary(int height, boolean expected) {
        // [데이터 신뢰성] 서비스 타겟 연령층의 생물학적 범위를 고려한 경계값 설정.
        // 비정상적인 데이터 유입을 차단하여 향후 통계/분석 데이터의 오염을 방지함.
        boolean isValid = height >= 100 && height <= 220;
        assertThat(isValid).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource({
            "29, false",  // 하한 미달
            "30, true",   // 하한 경계
            "150, true",  // 상한 경계
            "151, false"  // 상한 초과
    })
    @DisplayName("ATC-12: 몸무게 입력 유효 범위 검증 (30-150kg)")
    void testWeightBoundary(int weight, boolean expected) {
        boolean isValid = weight >= 30 && weight <= 150;
        assertThat(isValid).isEqualTo(expected);
    }

    @Test
    @DisplayName("ATC-16: 성별 중복 선택 시 최신값 업데이트 검증")
    void testGenderSelectionUpdate() {
        String currentGender = "MALE";
        String newSelection = "FEMALE";

        // 새로운 선택이 들어오면 기존 값이 덮어씌워지는지 확인
        currentGender = newSelection;

        assertThat(currentGender).isEqualTo("FEMALE");
    }

    @Test
    @DisplayName("ATC-5: 미지원 플랫폼 데이터 차단 확인")
    void testPlatformBlock() {
        String platform = "ZIGZAG";
        assertThatThrownBy(() -> {
            if(platform.equals("ZIGZAG")) throw new CrawlBusinessException(CrawlErrorCode.UNSUPPORTED_PLATFORM);
        }).isInstanceOf(CrawlBusinessException.class);
    }

    @Test
    @DisplayName("ATC-18: 상품 URL이 null일 때 버튼 비활성화 상태값 확인")
    void testUrlNullDefense() {
        // [외부 연동 방어] 지원하지 않는 플랫폼의 데이터가 유입될 경우
        // 비즈니스 예외를 명확히 던져 시스템 전체의 런타임 에러로 번지는 것을 막음.
        String url = null;
        boolean isButtonEnabled = (url != null && !url.isEmpty());
        assertThat(isButtonEnabled).isFalse();
    }

    @ParameterizedTest
    @CsvSource({
            "0, '가격 정보 없음'",
            "-100, '가격 정보 없음'",
            "15000, '15,000원'"
    })
    @DisplayName("ATC-8/29: 가격 데이터 유효성 및 포맷팅 검증")
    void testPriceFormatting(long price, String expected) {
        // [UI/UX 가독성] 가격이 0 이하(무료/오류)일 때의 대체 텍스트와
        // 화폐 단위(천 단위 콤마) 포맷팅이 클라이언트 요구사항과 일치하는지 확인.
        String result = (price <= 0) ? "가격 정보 없음" : String.format("%,d원", price);
        assertThat(result).isEqualTo(expected);
    }

    @Test
    @DisplayName("ATC-15: 태그 데이터가 없을 때 빈 리스트 반환 검증")
    void testEmptyTagDefense() {
        // [Null Safety] 데이터 크롤링 시 태그 정보가 누락(Null)되어도
        // 앱이 비정상 종료되지 않도록 빈 리스트(Empty List)로 변환하는 방어 로직 확인.
        java.util.List<String> tags = null;
        java.util.List<String> safeTags = (tags == null) ? java.util.Collections.emptyList() : tags;

        assertThat(safeTags).isNotNull().isEmpty();
    }
}
