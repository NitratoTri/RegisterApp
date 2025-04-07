package com.example.RegisterApp.Repositorys;

import com.example.RegisterApp.model.User;
import com.example.RegisterApp.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Test
    void testFindByEmail() {
        // Arrange
        User user = new User("testuser", "password", "test@example.com", "Test", "User");
        userRepository.save(user);

        // Act
        User foundUser = userRepository.findByEmail("test@example.com");

        // Assert
        Assertions.assertThat(foundUser).isNotNull();
        Assertions.assertThat(foundUser.getEmail()).isEqualTo("test@example.com");
    }

    @Test
    void testSaveUser() {
        // Arrange
        User user = new User("newuser", "password123", "new@example.com", "New", "User");

        // Act
        User savedUser = userRepository.save(user);

        // Assert
        Assertions.assertThat(savedUser).isNotNull();
        Assertions.assertThat(savedUser.getId()).isNotNull(); // ID generado automáticamente
        Assertions.assertThat(savedUser.getEmail()).isEqualTo("new@example.com");
    }
}
