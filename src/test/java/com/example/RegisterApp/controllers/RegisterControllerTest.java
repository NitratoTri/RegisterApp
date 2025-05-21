package com.example.RegisterApp.controllers;

import com.example.RegisterApp.controllers.RegisterController;
import com.example.RegisterApp.model.User;
import com.example.RegisterApp.repository.RoleRepository;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RegisterControllerTest {

    @InjectMocks
    private RegisterController registerController;

    @Mock
    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private RoleRepository roleRepository;

    @Test
    void testSuccessfulRegistration() {
        // Arrange
        User user = new User("testuser", "password", "test@example.com", "Test", "User");
        when(userService.findByEmail(user.getEmail())).thenReturn(null);
        when(passwordEncoder.encode(user.getPassword())).thenReturn("encodedPassword");
        when(userService.createUser(any(User.class))).thenReturn(user);

        // Act
        ResponseEntity<?> response = registerController.register(user);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(userService).createUser(any(User.class));
    }

    @Test
    void testRegistrationWithExistingEmail() {
        // Arrange
        User user = new User("testuser", "password", "existing@example.com", "Test", "User");
        when(userService.findByEmail(user.getEmail())).thenReturn(new User());

        // Act
        ResponseEntity<?> response = registerController.register(user);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
