package com.example.RegisterApp.repository;

import com.example.RegisterApp.model.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    @Test
    void testFindByName() {
        // Arrange: Verificar si el rol ya existe antes de guardarlo.
        Role role = roleRepository.findByName("ROLE_ADMIN");
        if (role == null) {
            role = new Role();
            role.setName("ROLE_ADMIN");
            role.setDescription("Administrator role");
            roleRepository.save(role);
        }

        // Act
        Role foundRole = roleRepository.findByName("ROLE_ADMIN");

        // Assert
        assertThat(foundRole).isNotNull();
        assertThat(foundRole.getName()).isEqualTo("ROLE_ADMIN");
    }

    @Test
    void testSaveRole() {
        // Arrange: Verificar si el rol ya existe antes de guardarlo.
        Role role = roleRepository.findByName("ROLE_USER");
        if (role == null) {
            role = new Role();
            role.setName("ROLE_USER");
            role.setDescription("User role");
            roleRepository.save(role);
        }

        // Act
        Role savedRole = roleRepository.findByName("ROLE_USER");

        // Assert
        assertThat(savedRole).isNotNull();
        assertThat(savedRole.getId()).isNotNull(); // ID generado automáticamente.
        assertThat(savedRole.getName()).isEqualTo("ROLE_USER");
    }
}
