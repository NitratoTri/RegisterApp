package com.example.RegisterApp.controllers;

import com.example.RegisterApp.model.User;
import com.example.RegisterApp.services.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserCrudControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserCrudController userCrudController;

    public UserCrudControllerTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetUser() {
        User user = new User();
        user.setId(1L);
        when(userService.getUser(1L)).thenReturn(user);

        ResponseEntity<User> response = userCrudController.getUser(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(user, response.getBody());
    }

    @Test
    void testGetAllUsers() {
        User user1 = new User();
        User user2 = new User();
        List<User> users = Arrays.asList(user1, user2);
        when(userService.getAllUsers()).thenReturn(users);

        ResponseEntity<List<User>> response = userCrudController.getAllUsers();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(users, response.getBody());
    }

    @Test
    void testCreateUser() {
        User user = new User();
        when(userService.createUser(user)).thenReturn(user);

        ResponseEntity<User> response = userCrudController.createUser(user);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(user, response.getBody());
    }

    @Test
    void testUpdateUser() {
        User user = new User();
        when(userService.updateUser(1L, user)).thenReturn(user);

        ResponseEntity<User> response = userCrudController.updateUser(1L, user);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(user, response.getBody());
    }

    @Test
    void testDeleteUser() {
        doNothing().when(userService).deleteUser(1L);

        ResponseEntity<Void> response = userCrudController.deleteUser(1L);

        assertEquals(204, response.getStatusCodeValue());
        verify(userService, times(1)).deleteUser(1L);
    }

    @Test
    void testAssignRoleToUser() {
        doNothing().when(userService).assignRoleToUserById(1L, 2L);

        ResponseEntity<Void> response = userCrudController.assignRoleToUser(1L, 2L);

        assertEquals(200, response.getStatusCodeValue());
        verify(userService, times(1)).assignRoleToUserById(1L, 2L);
    }
}