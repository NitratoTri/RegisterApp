package com.example.RegisterApp.controllers;

import com.example.RegisterApp.model.User;
import com.example.RegisterApp.security.JwtUtil;
import com.example.RegisterApp.services.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserCrudController.class)
class UserCrudControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private JwtUtil jwtUtil;

    @MockitoBean
    private UserService userService;

    @WithMockUser(roles = "ADMIN")
    @Test
    void testGetUser() throws Exception {
        User user = new User("johndoe", "password123", "john.doe@example.com", "John", "Doe");
        Mockito.when(userService.getUser(1L)).thenReturn(user);

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("johndoe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"))
                .andExpect(jsonPath("$.name").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));
    }
    @WithMockUser(roles = "ADMIN")
    @Test
    void testGetAllUsers() throws Exception {
        List<User> users = Arrays.asList(
                new User("johndoe", "password123", "john.doe@example.com", "John", "Doe"),
                new User("janedoe", "password123", "jane.doe@example.com", "Jane", "Doe")
        );
        Mockito.when(userService.getAllUsers()).thenReturn(users);

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].username").value("johndoe"))
                .andExpect(jsonPath("$[1].username").value("janedoe"));
    }
    @WithMockUser(roles = "ADMIN")
    @Test
    void testCreateUser() throws Exception {
        User user = new User("johndoe", "password123", "john.doe@example.com", "John", "Doe");
        Mockito.when(userService.createUser(any(User.class))).thenReturn(user);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"johndoe\",\"password\":\"password123\",\"email\":\"john.doe@example.com\",\"name\":\"John\",\"lastName\":\"Doe\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("johndoe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"))
                .andExpect(jsonPath("$.name").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));
    }
    @WithMockUser(roles = "ADMIN")
    @Test
    void testUpdateUser() throws Exception {
        User user = new User("johndoe", "password123", "john.doe@example.com", "John", "Doe Updated");
        Mockito.when(userService.updateUser(eq(1L), any(User.class))).thenReturn(user);

        mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"johndoe\",\"password\":\"password123\",\"email\":\"john.doe@example.com\",\"name\":\"John\",\"lastName\":\"Doe Updated\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("johndoe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"))
                .andExpect(jsonPath("$.name").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe Updated"));
    }
    @WithMockUser(roles = "ADMIN")
    @Test
    void testDeleteUser() throws Exception {
        Mockito.doNothing().when(userService).deleteUser(1L);

        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isNoContent());
    }
    @WithMockUser(roles = "ADMIN")
    @Test
    void testAssignRoleToUser() throws Exception {
        Mockito.doNothing().when(userService).assignRoleToUserById(1L, 2L);

        mockMvc.perform(post("/api/users/1/roles/2"))
                .andExpect(status().isOk());
    }
}