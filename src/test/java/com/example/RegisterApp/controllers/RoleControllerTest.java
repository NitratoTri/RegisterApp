package com.example.RegisterApp.controllers;

import com.example.RegisterApp.model.Role;
import com.example.RegisterApp.services.RoleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class RoleControllerTest {

    @Mock
    private RoleService roleService;

    @InjectMocks
    private RoleController roleController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllRoles() {
        Role role1 = new Role();
        Role role2 = new Role();
        List<Role> roles = Arrays.asList(role1, role2);

        when(roleService.getAllRoles()).thenReturn(roles);

        ResponseEntity<List<Role>> response = roleController.getAllRoles();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(roles, response.getBody());
    }

    @Test
    void testGetRoleById() {
        Role role = new Role();
        when(roleService.getRoleById(1L)).thenReturn(role);

        ResponseEntity<Role> response = roleController.getRoleById(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(role, response.getBody());
    }

    @Test
    void testCreateRole() {
        Role role = new Role();
        when(roleService.createRole(role)).thenReturn(role);

        ResponseEntity<Role> response = roleController.createRole(role);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(role, response.getBody());
    }

    @Test
    void testUpdateRole() {
        Role role = new Role();
        when(roleService.updateRole(1L, role)).thenReturn(role);

        ResponseEntity<Role> response = roleController.updateRole(1L, role);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(role, response.getBody());
    }

    @Test
    void testDeleteRole() {
        doNothing().when(roleService).deleteRole(1L);

        ResponseEntity<Void> response = roleController.deleteRole(1L);

        assertEquals(204, response.getStatusCodeValue());
        assertNull(response.getBody());
    }

    @Test
    void testAssignPermissionToRole() {
        doNothing().when(roleService).assingPermissionToRoleById(1L, 2L);

        ResponseEntity<Void> response = roleController.assignPermissionToRole(1L, 2L);

        assertEquals(200, response.getStatusCodeValue());
        assertNull(response.getBody());
    }
}