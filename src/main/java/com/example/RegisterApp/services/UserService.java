package com.example.RegisterApp.services;

import com.example.RegisterApp.model.Role;
import com.example.RegisterApp.model.User;
import com.example.RegisterApp.dto.UserDto;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public interface UserService {
    void saveUser(UserDto userDto);

    User findByEmail(String email);

    List<UserDto> findAllUsers();

    public UserDto convertEntityToDto(User user);
    public void save(User user);

    public Collection<Role> conseguirRolesByUser(User user);
    public void saveCifrandoPassword(User user);

    void assignRoleToUserById(Long id, Long roleId);
    void assignRoleToUserByRoleName(Long id, String roleName);

    User getUser(Long id);
    List<User> getAllUsers();
    User createUser(User user);
    User updateUser(Long id, User user);
    void deleteUser(Long id);

    UserDetails loadUserByUsername(String email);

    List<String> getRolesByUserId(Long userId);
}
