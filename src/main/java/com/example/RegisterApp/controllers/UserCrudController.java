package com.example.RegisterApp.controllers;

import com.example.RegisterApp.model.User;
import com.example.RegisterApp.repository.UserRepository;
import com.example.RegisterApp.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "User", description = "User management API")
public class UserCrudController {

    @Autowired
    private UserService userService;


    @Operation(summary = "Get a user by ID")
    @ApiResponse(responseCode = "200", description = "Successful operation")
    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUser(id));
    }

    @Operation(summary = "Get all users")
    @ApiResponse(responseCode = "200", description = "Successful operation")
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @Operation(summary = "Get a user by email")
    @ApiResponse(responseCode = "200", description = "Successful operation")
    @GetMapping("/email/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        User user = userService.findByEmail(email);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Create a new user")
    @ApiResponse(responseCode = "200", description = "User created")
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        return ResponseEntity.ok(userService.createUser(user));
    }

    @Operation(summary = "Update a user")
    @ApiResponse(responseCode = "200", description = "User updated")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        return ResponseEntity.ok(userService.updateUser(id, user));
    }

    @Operation(summary = "Delete a user")
    @ApiResponse(responseCode = "204", description = "User deleted")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Assign a role to a user")
    @ApiResponse(responseCode = "200", description = "Role assigned to user")
    @PostMapping("/{userId}/roles/{roleId}")
    public ResponseEntity<Void> assignRoleToUser(@PathVariable Long userId, @PathVariable Long roleId) {
        userService.assignRoleToUserById(userId, roleId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Get all roles assigned to a user")
    @ApiResponse(responseCode = "200", description = "All roles assigned to user")
    @GetMapping("/{userId}/roles")
    public ResponseEntity<List<String>> getRolesByUserId(@PathVariable Long userId) {
        List<String> roles = userService.getRolesByUserId(userId);
        if (roles.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(roles);
    }
  
}