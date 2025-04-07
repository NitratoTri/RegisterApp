package com.example.RegisterApp.controllers;

import com.example.RegisterApp.config.ErrorResponse;
import com.example.RegisterApp.model.Role;
import com.example.RegisterApp.model.User;
import com.example.RegisterApp.model.UserRole;
import com.example.RegisterApp.repository.RoleRepository;
import com.example.RegisterApp.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Date;
import java.util.List;


@RestController
@RequestMapping("/api/register")
@Tag(name = "Register", description = "Register managmente API")
public class RegisterController {
    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepository roleRepository;

    @Operation(summary = "Register a new user")
    @ApiResponse(responseCode = "200", description = "User created")
    @PostMapping
    public ResponseEntity<?> register(@Valid @RequestBody User user) {
        if (userService.findByEmail(user.getEmail()) != null) {
            return ResponseEntity.badRequest().build();
        }else if (userService.getAllUsers().stream().anyMatch(user1 -> user1.getEmail().equals(user.getEmail()))) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(
                            new Date(System.currentTimeMillis()),
                            "Email already exists",
                            "The provided email address is already registered in the system"
                    ));
        }
         else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setUserRoles(roleRepository.findByName("ROLE_USER"));
            return ResponseEntity.ok(userService.createUser(user));
        }


    }

}
