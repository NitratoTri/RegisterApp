package com.example.RegisterApp.controllers;

import com.example.RegisterApp.controllers.PermissionsController;
import com.example.RegisterApp.model.Permissions;
import com.example.RegisterApp.services.PermissionsService;
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

public class PermissionsControllerTest {

    @Mock
    private PermissionsService permissionsService;

    @InjectMocks
    private PermissionsController permissionsController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllPermissions() {
        Permissions p1 = new Permissions();
        Permissions p2 = new Permissions();
        List<Permissions> permissionsList = Arrays.asList(p1, p2);

        when(permissionsService.getAllPermissions()).thenReturn(permissionsList);

        ResponseEntity<List<Permissions>> response = permissionsController.getAllPermissions();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());
        verify(permissionsService, times(1)).getAllPermissions();
    }

    @Test
    void testGetPermissionById_Found() {
        Permissions permission = new Permissions();
        when(permissionsService.getPermissionById(1L)).thenReturn(permission);

        ResponseEntity<Permissions> response = permissionsController.getPermissionById(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(permission, response.getBody());
        verify(permissionsService, times(1)).getPermissionById(1L);
    }

    @Test
    void testGetPermissionById_NotFound() {
        when(permissionsService.getPermissionById(2L)).thenReturn(null);

        ResponseEntity<Permissions> response = permissionsController.getPermissionById(2L);

        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
        verify(permissionsService, times(1)).getPermissionById(2L);
    }

    @Test
    void testCreatePermission() {
        Permissions permission = new Permissions();

        doNothing().when(permissionsService).createPermission(permission);

        ResponseEntity<Permissions> response = permissionsController.createPermission(permission);

        assertEquals(201, response.getStatusCodeValue());
        assertEquals(permission, response.getBody());
        verify(permissionsService, times(1)).createPermission(permission);
    }

    @Test
    void testUpdatePermission() {
        Permissions permission = new Permissions();

        doNothing().when(permissionsService).updatePermission(1L, permission);

        ResponseEntity<Permissions> response = permissionsController.updatePermission(1L, permission);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(permission, response.getBody());
        verify(permissionsService, times(1)).updatePermission(1L, permission);
    }

    @Test
    void testDeletePermission() {
        doNothing().when(permissionsService).deletePermission(1L);

        ResponseEntity<Void> response = permissionsController.deletePermission(1L);

        assertEquals(204, response.getStatusCodeValue());
        assertNull(response.getBody());
        verify(permissionsService, times(1)).deletePermission(1L);
    }
}