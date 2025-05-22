package com.example.RegisterApp.repository;


import com.example.RegisterApp.model.Permissions;
import com.example.RegisterApp.model.Role;
import com.example.RegisterApp.model.RolePermissions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
public class RolePermissionRepoTest {
    //Test the methods of RolePermissionRepository

   //Autowiring the RolePermissionRepository and all other repositories for @BeforeEach
    @Autowired
    private RolePermissionRepo rolePermissionRepository;
    @Autowired
    private UserRoleRepository userRoleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PermissionRepository permissionRepository;

    @BeforeEach
    void setUp() {
        // Limpiar la base de datos antes de cada prueba
        userRoleRepository.deleteAll();
        userRepository.deleteAll();
        roleRepository.deleteAll();
        permissionRepository.deleteAll();
        rolePermissionRepository.deleteAll();
    }
    //Test Save and FindById
    @Test
    void testSaveAndFindById() {
        // Arrange
        //Crear un rol y un permiso para las pruebas
        // Verificar si el rol ya existe
        Role role = roleRepository.findByName("ROLE_USER");
        if (role == null) {
            role = new Role();
            role.setName("ROLE_USER");
            role.setDescription("User role");
            role = roleRepository.save(role);
        }
        // Verificar si el permiso ya existe
        Permissions permission = permissionRepository.findByName("READ");
        if (permission == null) {
            permission = new Permissions();
            permission.setName("READ");
            permission = permissionRepository.save(permission);
        }
        // Crear una instancia de RolePermissions
        RolePermissions rolePermission = new RolePermissions();
        rolePermission.setRole(role);
        rolePermission.setPermission(permission);

        // Act
        RolePermissions savedRolePermission = rolePermissionRepository.save(rolePermission);
        Optional<RolePermissions> foundRolePermission = rolePermissionRepository.findById(savedRolePermission.getId());

        // Assert
        assertTrue(foundRolePermission.isPresent());
        assertEquals(savedRolePermission.getId(), foundRolePermission.get().getId());
    }
    //Test FindAll

    @Test
    void testFindAll() {
        //Arrange

        //Crear un rol y un permiso para las pruebas
        // Verificar si el rol ya existe
        Role role = roleRepository.findByName("ROLE_USER");
        if (role == null) {
            role = new Role();
            role.setName("ROLE_USER");
            role.setDescription("User role");
            role = roleRepository.save(role);
        }
        // Verificar si el permiso ya existe
        Permissions permission = permissionRepository.findByName("READ");
        if (permission == null) {
            permission = new Permissions();
            permission.setName("READ");
            permission = permissionRepository.save(permission);
        }
        // Crear una instancia de RolePermissions
        RolePermissions rolePermission = new RolePermissions();
        rolePermission.setRole(role);
        rolePermission.setPermission(permission);

        //Act
        RolePermissions savedRolePermission = rolePermissionRepository.save(rolePermission);
        List<RolePermissions> foundAllRolePermission = rolePermissionRepository.findAll();
        //Assert
        assertTrue(foundAllRolePermission.size() >= 1);
        assertEquals(savedRolePermission.getId(), foundAllRolePermission.get(0).getId());
    }

    //Test Delete
    @Test
    void testDelete(){
        //Arrange
        //Crear un rol y un permiso para las pruebas
        // Verificar si el rol ya existe
        Role role = roleRepository.findByName("ROLE_USER");
        if (role == null) {
            role = new Role();
            role.setName("ROLE_USER");
            role.setDescription("User role");
            role = roleRepository.save(role);
        }
        // Verificar si el permiso ya existe
        Permissions permission = permissionRepository.findByName("READ");
        if (permission == null) {
            permission = new Permissions();
            permission.setName("READ");
            permission = permissionRepository.save(permission);
        }
        // Crear una instancia de RolePermissions
        RolePermissions rolePermission = new RolePermissions();
        rolePermission.setRole(role);
        rolePermission.setPermission(permission);
        //Act
        RolePermissions savedRolePermission = rolePermissionRepository.save(rolePermission);
        rolePermissionRepository.delete(savedRolePermission);

        //Assert
        Optional<RolePermissions> foundRolePermission = rolePermissionRepository.findById(savedRolePermission.getId());
        assertTrue(foundRolePermission.isEmpty());
    }
}
