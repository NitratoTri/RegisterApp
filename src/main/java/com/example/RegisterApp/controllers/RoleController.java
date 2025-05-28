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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@SecurityRequirement(name = "bearerAuth")
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
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping
    public ResponseEntity<Role> createRole(Role role){
        return ResponseEntity.ok(roleService.createRole(role));
    }
    @Operation(summary = "Update a role")
    @ApiResponse(responseCode = "200", description = "Role updated")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Role> updateRole(@PathVariable Long id, @RequestBody Role role) {
        return ResponseEntity.ok(roleService.updateRole(id, role));
    }

    @Operation(summary = "Delete a role")
    @ApiResponse(responseCode = "204", description = "Role deleted")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRole(Long id) {
        roleService.deleteRole(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Assign permission to role")
    @ApiResponse(responseCode = "200", description = "Permission assigned to role")
    @PutMapping("/{id}/permissions/{permissionId}")
    public ResponseEntity<Void> assignPermissionToRole(@PathVariable Long id, @PathVariable Long permissionId) {
        Role role = roleService.getRoleById(id);
        if (role == null) {
            return ResponseEntity.notFound().build();
        }
        if (roleService.getRoleById(id).getRolePermissions().stream()
                .anyMatch(rp -> rp.getPermission().getId().equals(permissionId))) {
            return ResponseEntity.badRequest().build();
        }
        roleService.assingPermissionToRoleById(id, permissionId);
        return ResponseEntity.ok().build();
    }
    @Operation(summary = "Show the relation between 1 role and permissions")
    @ApiResponse(responseCode = "200", description = "All permissions assigned to role")
    @PutMapping("/{id}/permissions")
    public ResponseEntity<List<String>> permissionsToThisRole(@PathVariable Long id) {
        Role role = roleService.getRoleById(id);
        if (role == null) {
            return ResponseEntity.notFound().build();
        }
        List<String> permissions = role.getRolePermissions().stream()
                .map(rp -> rp.getPermission().getName())
                .toList();
        return ResponseEntity.ok(permissions);
    }

   @Operation(summary = "Show the relation between all roles and permissions")
   @ApiResponse(responseCode = "200", description = "All roles with their permissions")
   @GetMapping("/rolepermissions")
   public ResponseEntity<List<Map<String, Object>>> getAllRolesWithPermissions() {
       List<Role> roles = roleService.getAllRoles();
       if (roles.isEmpty()) {
           return ResponseEntity.noContent().build();
       }
       List<Map<String, Object>> result = roles.stream().map(role -> {
           Map<String, Object> map = new HashMap<>();
           map.put("role", role.getName());
           List<String> permissions = role.getRolePermissions().stream()
                   .map(rp -> rp.getPermission().getName())
                   .toList();
           map.put("permissions", permissions);
           return map;
       }).toList();
       return ResponseEntity.ok(result);
   }

 @Operation(summary = "Asignar varios permisos a un rol")
 @ApiResponse(responseCode = "200", description = "Permisos asignados al rol")
 @PutMapping("/{id}/permissions/multiple")
 public ResponseEntity<Void> assignMultiplePermissionsToRole(@PathVariable Long id, @RequestBody List<Long> permissionIds) {
     Role role = roleService.getRoleById(id);
     if (role == null) {
         return ResponseEntity.notFound().build();
     }
     boolean anyAlreadyAssigned = permissionIds.stream()
             .anyMatch(permissionId -> role.getRolePermissions().stream()
                     .anyMatch(rp -> rp.getPermission().getId().equals(permissionId)));
     if (anyAlreadyAssigned) {
         return ResponseEntity.badRequest().build();
     }
     permissionIds.forEach(permissionId -> roleService.assingPermissionToRoleById(id, permissionId));
     return ResponseEntity.ok().build();
 }

}
