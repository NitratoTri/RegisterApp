package com.example.RegisterApp.controllers;

import com.example.RegisterApp.controllers.LoginController;
import com.example.RegisterApp.model.User;
import com.example.RegisterApp.repository.UserRepository;
import com.example.RegisterApp.security.JwtUtil;
import com.example.RegisterApp.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoginControllerTest {

    @InjectMocks
    private LoginController loginController;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    void testSuccessfulLogin() {
        // Arrange
        String email = "test@example.com";
        String password = "password";
        User user = new User();
        user.setEmail(email);
        user.setPassword("encodedPassword");

        when(userRepository.findByEmail(email)).thenReturn(user);
        when(passwordEncoder.matches(password, user.getPassword())).thenReturn(true);
        when(jwtUtil.generateToken(user)).thenReturn("dummyToken");
        // Act
        ResponseEntity<String> response = loginController.login(email, password);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("dummyToken", response.getBody()); // Ajusta el valor esperado según el formato real del token
    }

    @Test
    void testFailedLogin() {
        // Arrange
        String email = "test@example.com";
        String password = "wrongPassword";

        when(userRepository.findByEmail(email)).thenReturn(null);

        // Act
        ResponseEntity<String> response = loginController.login(email, password);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Invalid email or password", response.getBody());
    }
}
