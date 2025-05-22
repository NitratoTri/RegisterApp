package com.example.RegisterApp.repository;

import com.example.RegisterApp.model.Permissions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class PermissionRepositoryTest {

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;
    @BeforeEach
    void setUp() {
        // Limpiar la base de datos antes de cada prueba
        userRoleRepository.deleteAll();
        userRepository.deleteAll();
        roleRepository.deleteAll();
        permissionRepository.deleteAll();
    }
    @Test
    public void testSaveAndFindById() {
        Permissions permission = new Permissions();
        permission.setName("READ");
        Permissions saved = permissionRepository.save(permission);

        Optional<Permissions> found = permissionRepository.findById(saved.getId());
        assertTrue(found.isPresent());
        assertEquals("READ", found.get().getName());
    }

    @Test
    public void testFindAll() {
        Permissions permission1 = new Permissions();
        permission1.setName("WRITE");
        Permissions permission2 = new Permissions();
        permission2.setName("DELETE");
        permissionRepository.save(permission1);
        permissionRepository.save(permission2);

         List<Permissions> permissions= permissionRepository.findAll();
        assertTrue(permissions.size() >= 2);
    }

    @Test
    public void testDelete() {
        Permissions permission = new Permissions();
        permission.setName("UPDATE");
        Permissions saved = permissionRepository.save(permission);

        permissionRepository.delete(saved);
        Optional<Permissions> found = permissionRepository.findById(saved.getId());
        assertFalse(found.isPresent());
    }
}
