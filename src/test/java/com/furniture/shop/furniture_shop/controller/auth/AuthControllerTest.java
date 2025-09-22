package com.furniture.shop.furniture_shop.controller.auth;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.furniture.shop.furniture_shop.repository.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;
    @Autowired UserRepository userRepository;
    @Autowired PasswordEncoder passwordEncoder;

    private String toJson(Object o) throws Exception {
        return objectMapper.writeValueAsString(o);
    }

    @Test
    @DisplayName("회원가입: 204 응답 & 비밀번호는 BCrypt로 저장된다")
    void signup_success() throws Exception {
        // given
        var body = toJson(new Signup("user1@example.com", "Password!23"));

        // when
        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isNoContent());

        // then
        var saved = userRepository.findByEmail("user1@example.com").orElseThrow();
        assertThat(saved.getPassword()).isNotEqualTo("Password!23");
        assertThat(passwordEncoder.matches("Password!23", saved.getPassword())).isTrue();
    }

    @Test
    @DisplayName("로그인: 올바른 자격증명 -> JWT 토큰 발급")
    void login_success_returnsJwt() throws Exception {
        // given: 먼저 회원가입
        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(new Signup("user2@example.com", "Password!23"))))
                .andExpect(status().isNoContent());

        // when
        var res = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(new Login("user2@example.com", "Password!23"))))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        // then
        JsonNode json = objectMapper.readTree(res.getResponse().getContentAsByteArray());
        assertThat(json.hasNonNull("token")).isTrue();
        assertThat(json.get("token").asText()).isNotBlank();
    }

    @Test
    @DisplayName("로그인 실패: 비밀번호 불일치면 401(또는 4xx) 응답")
    void login_fail_wrong_password() throws Exception {
        // given
        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(new Signup("user3@example.com", "Password!23"))))
                .andExpect(status().isNoContent());

        // when + then
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(new Login("user3@example.com", "wrong-pass"))))
                // ⚠️ 예외 매핑을 안 하면 500이 날 수 있음. 아래 두 줄 중 상황에 맞춰 사용하세요.
                .andExpect(status().isUnauthorized());     // 추천: GlobalExceptionHandler 추가 시
        // .andExpect(status().is4xxClientError()); // 임시: 예외 매핑 전
    }

    // --- 테스트용 DTO (레코드) ---
    record Signup(String email, String password) {}
    record Login(String email, String password) {}
}
