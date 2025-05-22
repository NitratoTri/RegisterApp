package com.example.RegisterApp.services;

import com.example.RegisterApp.dto.UserDto;
import com.example.RegisterApp.model.Role;
import com.example.RegisterApp.model.User;
import com.example.RegisterApp.model.UserRole;
import com.example.RegisterApp.repository.RoleRepository;
import com.example.RegisterApp.repository.UserRepository;
import com.example.RegisterApp.repository.UserRoleRepository;
import com.example.RegisterApp.services.imp.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private UserRoleRepository userRoleRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;


        @BeforeEach
        void setUp() {
        userService = new UserServiceImpl(userRepository, roleRepository, passwordEncoder);
        // Usa reflexión para acceder a userRoleRepository si no es público
        try {
            java.lang.reflect.Field field = UserServiceImpl.class.getDeclaredField("userRoleRepository");
            field.setAccessible(true);
            field.set(userService, userRoleRepository);
        } catch (Exception e) {
            throw new RuntimeException(e);
         }
        }
    @Test
    void testSaveUser() {
        UserDto userDto = new UserDto();
        userDto.setFirstName("Juan");
        userDto.setLastName("Pérez");
        userDto.setEmail("juan@example.com");
        userDto.setPassword("1234");

        Role role = new Role();
        when(passwordEncoder.encode(anyString())).thenReturn("encoded");
        when(roleRepository.findByName("ROLE_USER")).thenReturn(role);

        userService.saveUser(userDto);

        verify(userRepository).save(any(User.class));
        verify(userRoleRepository).save(any(UserRole.class));
    }

    @Test
    void testSave() {
        User user = new User();
        userService.save(user);
        verify(userRepository).save(user);
    }

    @Test
    void testSaveCifrandoPassword() {
        User user = new User();
        user.setPassword("plain");
        when(passwordEncoder.encode("plain")).thenReturn("encoded");
        userService.saveCifrandoPassword(user);
        assertEquals("encoded", user.getPassword());
        verify(userRepository).save(user);
    }

    @Test
    void testAssignRoleToUserById() {
        User user = new User();
        Role role = new Role();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(roleRepository.findById(2L)).thenReturn(Optional.of(role));
        userService.assignRoleToUserById(1L, 2L);
        verify(userRoleRepository).save(any(UserRole.class));
    }

    @Test
    void testAssignRoleToUserByRoleName() {
        User user = new User();
        Role role = new Role();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(roleRepository.findByName("ROLE_ADMIN")).thenReturn(role);
        userService.assignRoleToUserByRoleName(1L, "ROLE_ADMIN");
        verify(userRoleRepository).save(any(UserRole.class));
    }

    @Test
    void testAssignRoleToUserByRoleNameThrows() {
        User user = new User();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(roleRepository.findByName("NO_EXISTE")).thenReturn(null);
        assertThrows(RuntimeException.class, () -> userService.assignRoleToUserByRoleName(1L, "NO_EXISTE"));
    }

    @Test
    void testFindByEmail() {
        User user = new User();
        when(userRepository.findByEmail("test@mail.com")).thenReturn(user);
        assertEquals(user, userService.findByEmail("test@mail.com"));
    }

    @Test
    void testFindAllUsers() {
        User user = new User();
        user.setName("Juan Pérez");
        user.setEmail("juan@example.com");
        user.setPassword("1234");
        List<User> users = Collections.singletonList(user);
        when(userRepository.findAll()).thenReturn(users);
        List<UserDto> dtos = userService.findAllUsers();
        assertEquals(1, dtos.size());
        assertEquals("Juan", dtos.get(0).getFirstName());
        assertEquals("Pérez", dtos.get(0).getLastName());
    }

    @Test
    void testConvertEntityToDto() {
        User user = new User();
        user.setName("Ana López");
        user.setEmail("ana@mail.com");
        user.setPassword("pass");
        UserDto dto = userService.convertEntityToDto(user);
        assertEquals("Ana", dto.getFirstName());
        assertEquals("López", dto.getLastName());
        assertEquals("ana@mail.com", dto.getEmail());
        assertEquals("pass", dto.getPassword());
    }

    @Test
    void testConseguirRolesByUser() {
        User user = new User();
        Role role = new Role();
        UserRole userRole = new UserRole(user, role);
        when(userRoleRepository.findByUser(user)).thenReturn(Collections.singletonList(userRole));
        Collection<Role> roles = userService.conseguirRolesByUser(user);
        assertTrue(roles.contains(role));
    }

    @Test
    void testGetUser() {
        User user = new User();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        assertEquals(user, userService.getUser(1L));
    }

    @Test
    void testGetUserThrows() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> userService.getUser(1L));
    }

    @Test
    void testGetAllUsers() {
        List<User> users = Arrays.asList(new User(), new User());
        when(userRepository.findAll()).thenReturn(users);
        assertEquals(2, userService.getAllUsers().size());
    }

    @Test
    void testCreateUser() {
        User user = new User();
        user.setUsername("user");
        user.setPassword("pass");
        user.setEmail("mail@mail.com");
        user.setLastName("Apellido");
        user.setName("Nombre");
        when(userRepository.save(any(User.class))).thenReturn(user);
        User result = userService.createUser(user);
        assertEquals("user", result.getUsername());
    }

    @Test
    void testUpdateUser() {
        User user = new User();
        user.setUsername("old");
        user.setEmail("old@mail.com");
        user.setName("Old Name");
        User details = new User();
        details.setUsername("new");
        details.setEmail("new@mail.com");
        details.setName("New Name");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);
        User updated = userService.updateUser(1L, details);
        assertEquals("new", updated.getUsername());
        assertEquals("new@mail.com", updated.getEmail());
        assertEquals("New Name", updated.getName());
    }

    @Test
    void testDeleteUser() {
        userService.deleteUser(1L);
        verify(userRepository).deleteById(1L);
    }

    @Test
    void testLoadUserByUsername() {
        User user = new User();
        user.setEmail("mail@mail.com");
        user.setPassword("pass");
        when(userRepository.findByEmail("mail@mail.com")).thenReturn(user);
        UserDetails details = userService.loadUserByUsername("mail@mail.com");
        assertEquals("mail@mail.com", details.getUsername());
    }

    @Test
    void testLoadUserByUsernameThrows() {
        when(userRepository.findByEmail("no@mail.com")).thenReturn(null);
        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername("no@mail.com"));
    }
}