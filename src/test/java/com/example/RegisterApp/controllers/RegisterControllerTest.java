package com.example.RegisterApp.controllers;

import com.example.RegisterApp.config.ErrorResponse;
import com.example.RegisterApp.model.Role;
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

import java.sql.Date;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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
        Role role = new Role("ROLE_USER", "Default role for users");
        when(userService.findByEmail(user.getEmail())).thenReturn(null);
        when(userService.getAllUsers()).thenReturn(Collections.emptyList());
        when(passwordEncoder.encode(user.getPassword())).thenReturn("encodedPassword");
        when(roleRepository.findByName("ROLE_USER")).thenReturn(role);
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

    @Test
    void testRegistrationWithDuplicateEmailInAllUsers() {
        // Arrange
        User user = new User("testuser", "password", "duplicate@example.com", "Test", "User");
        User existingUser = new User("existinguser", "password", "duplicate@example.com", "Existing", "User");
        when(userService.findByEmail(user.getEmail())).thenReturn(null);
        when(userService.getAllUsers()).thenReturn(List.of(existingUser));

        // Act
        ResponseEntity<?> response = registerController.register(user);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody() instanceof ErrorResponse);
        ErrorResponse errorResponse = (ErrorResponse) response.getBody();
        assertEquals("Email already exists", errorResponse.getMessage());
    }
}