package com.example.RegisterApp.controllers;

import com.example.RegisterApp.model.Permissions;
import com.example.RegisterApp.services.PermissionsService;
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
@Tag(name = "Permission", description = "Permission management API")
@RequestMapping("/api/permissions")
public class PermissionsController {
    //Controller for managing user permissions
    // Methods for managing permissions will be added here, like creating, updating, deleting permissions, list permissions, etc.

    @Autowired
    private PermissionsService permissionsService;

    @Operation(summary = "Get all Permissions")
    @ApiResponse(responseCode = "200", description = "Successful operation")
    @GetMapping
    public ResponseEntity<List<Permissions>> getAllPermissions() {
        List<Permissions> permissionsList = permissionsService.getAllPermissions();
        return ResponseEntity.ok(permissionsList);
    }
    @Operation(summary = "Get Permission by ID")
    @ApiResponse(responseCode = "200", description = "Successful operation")
    @GetMapping("/{id}")
    public ResponseEntity<Permissions> getPermissionById(@PathVariable Long id) {
        Permissions permission = permissionsService.getPermissionById(id);
        if (permission != null) {
            return ResponseEntity.ok(permission);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Create a new Permission")
    @ApiResponse(responseCode = "201", description = "Permission created")
    @PostMapping
    public ResponseEntity<Permissions> createPermission(@RequestBody Permissions permission) {
        permissionsService.createPermission(permission);
        return ResponseEntity.status(201).body(permission);
    }

    @Operation(summary = "Update a Permission")
    @ApiResponse(responseCode = "200", description = "Permission updated")
    @PutMapping("/{id}")
    public ResponseEntity<Permissions> updatePermission(@PathVariable Long id, @RequestBody Permissions permission) {
        permissionsService.updatePermission(id, permission);
        return ResponseEntity.ok(permission);
    }
    @Operation(summary = "Delete a Permission")
    @ApiResponse(responseCode = "204", description = "Permission deleted")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePermission(@PathVariable Long id) {
        permissionsService.deletePermission(id);
        return ResponseEntity.noContent().build();
    }
    //TO-DO: Add methods for assigning permissions to roles, etc.
}
