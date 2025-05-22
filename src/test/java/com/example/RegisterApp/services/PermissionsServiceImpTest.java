package com.example.RegisterApp.services;

import com.example.RegisterApp.model.Permissions;
import com.example.RegisterApp.repository.*;
import com.example.RegisterApp.services.imp.PermissionsServiceImp;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class PermissionsServiceImpTest {

    @Mock
    private UserRoleRepository userRoleRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private PermissionRepository permissionRepository;
    @InjectMocks
    private PermissionsServiceImp permissionsServiceImp;


    @Test
    void testCreatePermission() {
        Permissions per = new Permissions();
        per.setName("READ");
        per.setDescription("Read permission");

        permissionsServiceImp.createPermission(per);

        verify(permissionRepository).save(any(Permissions.class));
    }

    @Test
    void testGetPermissionById() {
        Permissions per = new Permissions();
        per.setName("WRITE");
        per.setDescription("Write permission");
        when(permissionRepository.findById(1L)).thenReturn(Optional.of(per));

        Permissions result = permissionsServiceImp.getPermissionById(1L);

        assertNotNull(result);
        assertEquals("WRITE", result.getName());
    }

    @Test
    void testUpdatePermission() {
        Permissions existing = new Permissions();
        existing.setName("OLD");
        existing.setDescription("Old desc");
        Permissions updated = new Permissions();
        updated.setName("NEW");
        updated.setDescription("New desc");

        when(permissionRepository.findById(1L)).thenReturn(Optional.of(existing));

        permissionsServiceImp.updatePermission(1L, updated);

        assertEquals("NEW", existing.getName());
        assertEquals("New desc", existing.getDescription());
        verify(permissionRepository).save(existing);
    }

    @Test
    void testUpdatePermissionNotFound() {
        Permissions updated = new Permissions();
        updated.setName("NEW");
        updated.setDescription("New desc");

        when(permissionRepository.findById(1L)).thenReturn(Optional.empty());

        permissionsServiceImp.updatePermission(1L, updated);

        verify(permissionRepository, never()).save(any(Permissions.class));
    }

    @Test
    void testDeletePermission() {
        permissionsServiceImp.deletePermission(1L);
        verify(permissionRepository).deleteById(1L);
    }

    @Test
    void testGetAllPermissions() {
        List<Permissions> list = Arrays.asList(new Permissions(), new Permissions());
        when(permissionRepository.findAll()).thenReturn(list);

        List<Permissions> result = permissionsServiceImp.getAllPermissions();

        assertEquals(2, result.size());
    }
}