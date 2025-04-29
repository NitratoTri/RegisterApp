package com.example.RegisterApp.controllers;

import com.example.RegisterApp.model.Role;
import com.example.RegisterApp.services.RoleService;
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
@SecurityRequirement(name = "bearerAuth")
@PreAuthorize("hasRole('ROLE_ADMIN')")
@Tag(name = "Role", description = "Role management API")
@RequestMapping("/api/roles")
public class RoleController {
//Controller for managing user roles
    @Autowired
    private RoleService roleService;
    // Add methods for role management here
    // For example, you can add methods to create, update, delete roles
    // and assign roles to users, etc.
    @Operation(summary = "Get all roles")
    @ApiResponse(responseCode = "200", description = "Successful operation")
    @GetMapping
    public ResponseEntity<List<Role>> getAllRoles() {
        return ResponseEntity.ok(roleService.getAllRoles());
    }
   @Operation(summary = "Get role by ID")
    @ApiResponse(responseCode = "200", description = "Successful operation")
    @GetMapping("/{id}")
    public ResponseEntity<Role> getRoleById(Long id) {
        return ResponseEntity.ok(roleService.getRoleById(id));
    }

    @Operation(summary = "Create a new role")
    @ApiResponse(responseCode = "201", description = "Role created")
    @PutMapping
    public ResponseEntity<Role> createRole(Role role){
        return ResponseEntity.ok(roleService.createRole(role));
    }
    @Operation(summary = "Update a role")
    @ApiResponse(responseCode = "200", description = "Role updated")
    @PutMapping("/{id}")
    public ResponseEntity<Role> updateRole(@PathVariable Long id, @RequestBody Role role) {
        return ResponseEntity.ok(roleService.updateRole(id, role));
    }

    @Operation(summary = "Delete a role")
    @ApiResponse(responseCode = "204", description = "Role deleted")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRole(Long id) {
        roleService.deleteRole(id);
        return ResponseEntity.noContent().build();
    }

    //TO-DO: Add methods for assigning roles to users, etc.

}
