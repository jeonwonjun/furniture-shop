package com.furniture.shop.furniture_shop.repository.user;

import com.furniture.shop.furniture_shop.model.user.Role;
import com.furniture.shop.furniture_shop.model.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")

class UserRepositoryTest {

    private final UserRepository userRepository;

    @Autowired
    UserRepositoryTest(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Test
    @DisplayName("User 저장 & 이메일 조회")
    void saveAndFindByEmail() {
        User u = new User("a@example.com", "pw", Role.USER, true);
        userRepository.save(u);

        assertThat(u.getId()).isNotNull();
        assertThat(userRepository.existsByEmail("a@example.com")).isTrue();
        assertThat(userRepository.findByEmail("a@example.com")).isPresent();
    }
}
