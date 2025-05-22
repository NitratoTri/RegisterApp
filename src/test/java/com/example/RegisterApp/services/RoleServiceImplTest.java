package com.example.RegisterApp.services;

import com.example.RegisterApp.model.Permissions;
import com.example.RegisterApp.model.Role;
import com.example.RegisterApp.model.RolePermissions;
import com.example.RegisterApp.repository.PermissionRepository;
import com.example.RegisterApp.repository.RolePermissionRepo;
import com.example.RegisterApp.repository.RoleRepository;
import com.example.RegisterApp.services.imp.RoleServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RoleServiceImplTest {

    @Mock
    private RoleRepository roleRepository;
    @Mock
    private PermissionRepository permissionRepository;
    @Mock
    private RolePermissionRepo rolePermissionRepo;
    @InjectMocks
    private RoleServiceImpl roleService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // No es necesario reasignar los repositorios si usas @InjectMocks y @Mock correctamente
    }

    @Test
    void testCreateRole() {
        Role role = new Role();
        role.setName("Admin");
        role.setDescription("Admin role");

        Role savedRole = new Role();
        savedRole.setName("Admin");
        savedRole.setDescription("Admin role");

        when(roleRepository.save(any(Role.class))).thenReturn(savedRole);

        Role result = roleService.createRole(role);

        assertNotNull(result);
        assertEquals("Admin", result.getName());
        assertEquals("Admin role", result.getDescription());
        verify(roleRepository, times(1)).save(any(Role.class));
    }

    @Test
    void testGetAllRoles() {
        Role role1 = new Role();
        Role role2 = new Role();
        when(roleRepository.findAll()).thenReturn(Arrays.asList(role1, role2));

        List<Role> roles = roleService.getAllRoles();

        assertEquals(2, roles.size());
        verify(roleRepository, times(1)).findAll();
    }

    @Test
    void testGetRoleById() {
        Role role = new Role();
        role.setName("User");
        when(roleRepository.findById(1L)).thenReturn(Optional.of(role));

        Role result = roleService.getRoleById(1L);

        assertNotNull(result);
        assertEquals("User", result.getName());
        verify(roleRepository, times(1)).findById(1L);
    }

    @Test
    void testUpdateRole_Found() {
        Role existingRole = new Role();
        existingRole.setName("Old");
        existingRole.setDescription("Old desc");

        Role updatedRole = new Role();
        updatedRole.setName("New");
        updatedRole.setDescription("New desc");

        when(roleRepository.findById(1L)).thenReturn(Optional.of(existingRole));
        when(roleRepository.save(any(Role.class))).thenReturn(updatedRole);

        Role result = roleService.updateRole(1L, updatedRole);

        assertNotNull(result);
        assertEquals("New", result.getName());
        assertEquals("New desc", result.getDescription());
        verify(roleRepository, times(1)).save(existingRole);
    }

    @Test
    void testUpdateRole_NotFound() {
        Role updatedRole = new Role();
        when(roleRepository.findById(1L)).thenReturn(Optional.empty());

        Role result = roleService.updateRole(1L, updatedRole);

        assertNull(result);
        verify(roleRepository, never()).save(any(Role.class));
    }

    @Test
    void testDeleteRole() {
        doNothing().when(roleRepository).deleteById(1L);

        roleService.deleteRole(1L);

        verify(roleRepository, times(1)).deleteById(1L);
    }

    @Test
    void testAssignPermissionToRoleById_Found() {
        Role role = new Role();
        Permissions permission = new Permissions();

        when(roleRepository.findById(1L)).thenReturn(Optional.of(role));
        when(permissionRepository.findById(2L)).thenReturn(Optional.of(permission));
        when(rolePermissionRepo.save(any(RolePermissions.class))).thenReturn(new RolePermissions());

        roleService.assingPermissionToRoleById(1L, 2L);

        verify(rolePermissionRepo, times(1)).save(any(RolePermissions.class));
    }

    @Test
    void testAssignPermissionToRoleById_NotFound() {
        when(roleRepository.findById(1L)).thenReturn(Optional.empty());
        when(permissionRepository.findById(2L)).thenReturn(Optional.empty());

        roleService.assingPermissionToRoleById(1L, 2L);
        verify(rolePermissionRepo, never()).save(any(RolePermissions.class));
    }
}