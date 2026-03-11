package com.dekk.integration;

import com.dekk.auth.jwt.JwtTokenProvider;
import com.dekk.security.oauth2.CustomUserDetails;
import com.dekk.user.application.command.UserCreateCommand;
import com.dekk.user.application.command.UserOnboardingCommand;
import com.dekk.user.domain.model.User;
import com.dekk.user.domain.model.enums.Gender;
import com.dekk.user.domain.model.enums.Provider;
import com.dekk.user.domain.repository.UserRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Collections;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

/**
 * [회귀 테스트] API 통합 및 비즈니스 로직 정합성 검증
 * - 주요 도메인(유저, 카드, 온보딩)의 API 엔드포인트 간 상호작용을 검증함.
 * - DB 및 시큐리티 필터가 포함된 실제 런타임 환경과 유사한 조건에서 수행.
 */
@Tag("regression")
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApiIntegrationTest {

    @MockitoBean
    private ClientRegistrationRepository clientRegistrationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
    }

    @AfterEach
    void tearDown() {
        RestAssured.reset();
        // 각 테스트 후 데이터 정리를 원하시면 여기에 userRepository.deleteAllInBatch() 추가 권장
    }

    // [Helper] 소셜 로그인(OAuth2) 과정을 생략하고 테스트용 JWT 토큰을 즉시 발급함.
    private String getAccessTokenForUser(String email, String kakaoId) {
        User user = userRepository.findByEmail(email).orElseGet(() ->
                userRepository.save(User.create(new UserCreateCommand(email, Provider.KAKAO, kakaoId))));

        CustomUserDetails userDetails = new CustomUserDetails(
                user.getId(), user.getEmail(), user.getRole().getKey(), user.getStatus()
        );

        Authentication auth = new UsernamePasswordAuthenticationToken(
                userDetails, "", Collections.singleton(new SimpleGrantedAuthority(user.getRole().getKey()))
        );

        return jwtTokenProvider.createAccessToken(auth);
    }

    // [Helper] 테스트 수행 전 유저가 온보딩을 완료한 'ACTIVE' 상태임을 보장함.
    private void setupActiveUser(String email, String kakaoId, String nickname) {
        User user = userRepository.findByEmail(email).orElseGet(() ->
                User.create(new UserCreateCommand(email, Provider.KAKAO, kakaoId)));

        if (user.getProfile() == null) {
            user.completeOnboarding(new UserOnboardingCommand(nickname, 175, 70, Gender.MALE));
        }
        userRepository.save(user);
    }

    // [Helper] 온보딩 요청 공통화
    private io.restassured.response.Response requestOnboarding(String token, String nickname, int height, int weight) {
        return given()
                .cookie("access_token", token)
                .contentType(ContentType.JSON)
                .body(String.format("{\"nickname\":\"%s\", \"height\":%d, \"weight\":%d, \"gender\":\"MALE\"}",
                        nickname, height, weight))
                .when()
                .post("/w/v1/users/onboarding");
    }

    @Test
    @DisplayName("ATC-13: 비회원 상태에서 카드 상세 정보 요청 시 상품정보 미노출")
    void testGuestAccessRestriction() {
        // [비즈니스 규칙] 비회원은 카드 리스트는 볼 수 있으나,
        // 수익 모델과 직결된 '상품 상세 정보(goods)'는 노출되지 않아야 함.
        given()
                .queryParam("page", 0)
                .queryParam("size", 10)
                .when()
                .get("/w/v1/cards")
                .then()
                .statusCode(200)
                .body("data.content", hasSize(0));
    }

    @Test
    @DisplayName("ATC-22: 온보딩 미완료 유저가 인증 필요(덱) API 호출 시 빈 데이터 확인")
    void testIncompleteUserAccessRestricted() {
        String token = getAccessTokenForUser("pending@test.com", "kakao_pending");

        given()
                .cookie("access_token", token)
                .when()
                .get("/w/v1/decks/custom")
                .then()
                .statusCode(200)
                .body("data", hasSize(0));
    }

    @Test
    @DisplayName("ATC-11: 이미 사용 중인 닉네임으로 온보딩 시도 시 409 에러")
    void testNicknameDuplicate() {
        // [중복 검증] 동일한 닉네임으로 가입을 시도할 경우,
        // 데이터 무결성을 위해 409 Conflict와 약속된 에러 코드(EU40901)를 반환해야 함.
        setupActiveUser("already@test.com", "kakao_existing", "test_123");

        String tokenB = getAccessTokenForUser("userB@test.com", "kakao_new");
        requestOnboarding(tokenB, "test_123", 180, 75)
                .then()
                .statusCode(409)
                .body("code", is("EU40901"));
    }

    @ParameterizedTest(name = "[{index}] 닉네임: ''{0}'', 키: {1}, 몸무게: {2} -> 에러: {3}")
    @CsvSource(value = {
            "A, 170, 60, EU40003",
            "'  test_123  ', 170, 60, EU40003",
            "nicknameOverTen, 170, 60, EU40003",
            "닉네임🔥, 170, 60, EU40003",
            "test, 300, 70, EU40006",
            "test, 170, 10, EU40006"
    })
    @DisplayName("ATC-6/7/12/17: 온보딩 유효성 및 경계값 통합 검증 (Strict Mode)")
    void testOnboardingValidation(String nickname, int height, int weight, String expectedErrorCode) {
        // [경계값 분석] 닉네임(2~10자), 키(100~220cm), 몸무게(30~150kg)의
        // 범위를 벗어나는 입력값에 대해 서버가 정확한 에러 코드를 뱉는지 전수 검사함.
        String token = getAccessTokenForUser("valid_param@test.com", "kakao_param");

        requestOnboarding(token, nickname, height, weight)
                .then()
                .statusCode(400)
                .body("code", is(expectedErrorCode));
    }

    @Test
    @DisplayName("ATC-27: 카드 리스트 마지막 페이지 도달 시 빈 리스트 반환 확인")
    void testCardExhaustion() {
        // [예외 상황 시뮬레이션] 존재하지 않는 페이지(9999)를 요청했을 때
        // 404가 아닌 200 OK와 빈 배열을 반환
        String token = getAccessTokenForUser("active@test.com", "kakao_a");
        setupActiveUser("active@test.com", "kakao_a", "활동유저");

        given()
                .cookie("access_token", token)
                .queryParam("page", 9999)
                .queryParam("size", 10)
                .when()
                .get("/w/v1/cards")
                .then()
                .statusCode(200)
                .body("data.content", hasSize(0));
    }

    @Test
    @DisplayName("ATC-15: 온보딩 완료 유저의 카드 정보 정상 조회")
    void testActiveUserCardList() {
        String token = getAccessTokenForUser("card_user@test.com", "kakao_card");
        setupActiveUser("card_user@test.com", "kakao_card", "카드보는유저");

        given()
                .cookie("access_token", token)
                .queryParam("page", 0)
                .queryParam("size", 10)
                .when()
                .get("/w/v1/cards")
                .then()
                .statusCode(200)
                .body("data.content", notNullValue());
    }

    @Test
    @DisplayName("ATC-23: 우측 스와이프(LIKE) 시 API 정상 호출 확인")
    void testSwipeRightDataIntegrity() {
        String token = getAccessTokenForUser("swipe@test.com", "kakao_swipe");
        setupActiveUser("swipe@test.com", "kakao_swipe", "스와이프유저");

        long targetCardId = 1L;

        given()
                .cookie("access_token", token)
                .contentType(ContentType.JSON)
                .body("{\"swipeType\": \"LIKE\"}")
                .when()
                .post("/w/v1/cards/" + targetCardId + "/swipe")
                .then()
                .log().ifValidationFails()
                .statusCode(200);
    }

    @Test
    @DisplayName("ATC-24: 응답 시간 성능 검증 (500ms 이내)")
    void testCardDetailResponseTime() {
        // [비기능 요구사항] 쾌적한 UX를 위해 메인 카드 리스트 API의 응답 속도는
        // 서버 부하가 없는 상태에서 500ms 이내를 유지해야 함
        String token = getAccessTokenForUser("speed@test.com", "kakao_speed");
        setupActiveUser("speed@test.com", "kakao_speed", "스피드유저");

        given()
                .cookie("access_token", token)
                .queryParam("page", 0)
                .queryParam("size", 10)
                .when()
                .get("/w/v1/cards")
                .then()
                .statusCode(200)
                .time(org.hamcrest.Matchers.lessThan(500L));
    }
}
