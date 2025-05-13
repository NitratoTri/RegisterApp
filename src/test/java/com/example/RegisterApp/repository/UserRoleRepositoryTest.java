package com.example.RegisterApp.repository;

import com.example.RegisterApp.model.Role;
import com.example.RegisterApp.model.User;
import com.example.RegisterApp.model.UserRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class UserRoleRepositoryTest {

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Test
    void testFindByUser() {
        // Arrange: Crear usuario y rol, luego asignar el rol al usuario.
        User user = new User("testuser", "password", "test@example.com", "Test", "User");
        user = userRepository.save(user);


        // Verificar si el rol ya existe
        Role role = roleRepository.findByName("ROLE_USER");
        if (role == null) {
            role = new Role();
            role.setName("ROLE_USER");
            role.setDescription("User role");
            role = roleRepository.save(role);
        }

        UserRole userRole = new UserRole(user, role);
        userRoleRepository.save(userRole);

        // Act: Buscar roles asignados al usuario.
        List<UserRole> foundRoles = userRoleRepository.findByUser(user);

        // Assert: Verificar que se encontró el rol correctamente.
        assertThat(foundRoles).isNotEmpty();
        assertThat(foundRoles.get(0).getRole().getName()).isEqualTo("ROLE_USER");
    }

    @Test
    void testSaveUserRole() {
        // Arrange: Crear usuario y rol, luego asignar el rol al usuario.
        User user = new User("testuser", "password", "test@example.com", "Test", "User");
        user = userRepository.save(user);


        // Verificar si el rol ya existe
        Role role = roleRepository.findByName("ROLE_USER");
        if (role == null) {
            role = new Role();
            role.setName("ROLE_USER");
            role.setDescription("User role");
            role = roleRepository.save(role);
        }

        UserRole userRole = new UserRole(user, role);

        // Act: Guardar la relación usuario-rol.
        UserRole savedUserRole = userRoleRepository.save(userRole);

        // Assert: Verificar que se guardó correctamente.
        assertThat(savedUserRole).isNotNull();
        assertThat(savedUserRole.getId()).isNotNull(); // ID generado automáticamente.
    }
}
