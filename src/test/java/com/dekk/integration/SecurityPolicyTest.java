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
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;

/**
 * [인가 정책 리그레션] 사용자 보안 및 접근 권한 검증
 * - 비회원, 온보딩 미완료자, 탈퇴 유저 등 상태별 API 접근 권한을 엄격히 감시함.
 * - 로그아웃 및 탈퇴 후 토큰 만료 여부 등 세션 보안의 무결성을 확인하는 것이 주 목적.
 */
@Tag("regression")
@Tag("security")
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SecurityPolicyTest {

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

        // 테스트 시작 전 RestAssured 설정을 초기화합니다.
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
    }

    @AfterEach
    void tearDown() {
        // 테스트가 끝난 후 설정을 초기화합니다.
        // 다음 테스트 파일에 영향을 주지 않기 위해 기본값으로 되돌립니다.
        RestAssured.reset();
    }

    // 헬퍼 메서드
    private String getAccessTokenForUser(String email, String kakaoId) {
        // 1. 이미 존재하는 유저면 그 유저를 사용하고, 없으면 생성 (중복 에러 방지)
        User user = userRepository.findByEmail(email).orElseGet(() ->
                userRepository.save(User.create(new UserCreateCommand(email, Provider.KAKAO, kakaoId))));

        CustomUserDetails userDetails = new CustomUserDetails(
                user.getId(),
                user.getEmail(),
                user.getRole().getKey(),
                user.getStatus()
        );

        // 3. 인증 객체 생성 및 토큰 발행
        Authentication auth = new UsernamePasswordAuthenticationToken(
                userDetails,
                "",
                Collections.singleton(new SimpleGrantedAuthority(user.getRole().getKey()))
        );

        return jwtTokenProvider.createAccessToken(auth);
    }

    private void setupActiveUser(String email, String kakaoId, String nickname) {
        User user = userRepository.findByEmail(email).orElseGet(() ->
                User.create(new UserCreateCommand(email, Provider.KAKAO, kakaoId)));

        // 이미 온보딩 되어있지 않은 경우에만 수행
        if (user.getProfile() == null) {
            user.completeOnboarding(new UserOnboardingCommand(nickname, 175, 70, Gender.MALE));
        }
        userRepository.save(user);
    }

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
    @DisplayName("ATC-14: 비회원은 스와이프 동작 시 DB(커스텀 덱)에 저장되지 않는다 (401 차단)")
    void testGuestSwipeAndDeckRestriction() {
        // [설계 의도] REST API는 인증 실패 시 페이지 리다이렉트(302)가 아닌
        // 명확한 에러 코드(401)를 반환하여 FE가 후속 제어를 할 수 있게 해야 함.
        long targetCardId = 1L;

        // 1. [검증] 비회원(토큰 없음)이 강제로 스와이프(LIKE) API를 호출해 DB 저장을 시도할 경우
        given()
                // 토큰 헤더(쿠키) 없음
                .redirects().follow(false) // 자동 리다이렉트를 방지하여 서버의 원본 응답 코드를 확인
                .contentType(ContentType.JSON)
                .body("{\"swipeType\": \"LIKE\"}")
                .when()
                .post("/w/v1/cards/" + targetCardId + "/swipe")
                .then()
                .log().all()
                // 시큐리티에 의해 인증되지 않은 사용자(401)로 튕겨나가 DB 저장이 원천 차단됨을 확인
                .statusCode(anyOf(is(401), is(403)));

        // 2. [검증] 비회원(토큰 없음)이 커스텀 덱 조회 API를 호출할 경우
        // [비즈니스 규칙] 덱 정보는 개인 자산이므로 인증되지 않은 접근 시 로그인 페이지로 유도(302)함이 정당함.
        given()
                // 토큰 헤더(쿠키) 없음
                .when()
                .get("/w/v1/decks/custom")
                .then()
                .log().all()
                .statusCode(302)
                .header("Location", containsString("/login"));
    }

    @Test
    @DisplayName("ATC-20: 소셜 로그인만 완료한 유저의 세션 상태(PENDING) 유지 확인")
    void testOnboardingSessionPending() {
        String token = getAccessTokenForUser("session@test.com", "kakao_session");

        given()
                .cookie("access_token", token)
                .when()
                .get("/w/v1/users/me") // 내 정보 조회 API
                .then()
                .statusCode(200)
                .body("data.status", is("PENDING"));
    }

    @Test
    @DisplayName("ATC-28: 로그아웃 후 기존 토큰으로 API 호출 시 401 에러 및 세션 파기 확인")
    void testLogoutInvalidatesToken() {
        // [보안 리스크] 로그아웃 후에도 토큰이 살아있다면 세션 하이재킹 위험이 있음.
        // 서버 측에서 해당 토큰을 블랙리스트 처리하거나 세션을 완전히 파기하는지 확인.
        String token = getAccessTokenForUser("logout@test.com", "kakao_logout");
        setupActiveUser("logout@test.com", "kakao_logout", "로그아웃유저");

        // 1. 로그아웃 API 호출 (정상적으로 200 OK 떨어지는지 확인)
        given()
                .cookie("access_token", token)
                .when()
                .post("/w/v1/auth/logout")
                .then()
                .statusCode(anyOf(is(200), is(204)));

        // 2. [When & Then] 방금 로그아웃한 토큰으로 내 정보 조회 API 찌르기
        // 로그아웃 직후 동일 토큰으로 접근 시 즉시 차단되어야 함
        given()
                .cookie("access_token", token)
                .when()
                .get("/w/v1/users/me")
                .then()
                .log().all()
                // 로그아웃된 토큰이므로 401 Unauthorized가 발생해야 함
                .statusCode(401);
    }

    @Test
    @DisplayName("ATC-10/21: 회원 탈퇴 후 재가입(온보딩) 시도 시 차단")
    void testWithdrawalUserFlow() {
        // [데이터 무결성] 탈퇴 처리된 유저(DELETED)는 기존 세션으로 서비스를 이용할 수 없어야 하며,
        // 재가입 전까지는 모든 유저 액션 API가 차단됨을 보장해야 함.
        String email = "rejoin@test.com";
        String token = getAccessTokenForUser(email, "kakao_rejoin");

        // 1. 온보딩
        requestOnboarding(token, "탈퇴예정", 175, 70)
                .then()
                .log().all()
                .statusCode(200);

        // 2. 실제 탈퇴 API 호출
        given()
                .cookie("access_token", token)
                .when()
                .delete("/w/v1/users/me")
                .then()
                .log().all()
                .statusCode(200);

        // 3. 상태 확인
        User afterWithdraw = userRepository.findByEmail(email).orElseThrow();
        System.out.println("User Status after delete: " + afterWithdraw.getStatus());

        // 4. 다시 온보딩 시도
        requestOnboarding(token, "재가입시도", 175, 70)
                .then()
                .log().all()
                .statusCode(anyOf(is(403), is(400), is(401)));
    }
}
