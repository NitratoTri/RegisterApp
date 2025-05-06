package com.example.RegisterApp.services;

import com.example.RegisterApp.model.Role;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RoleService {
    //Service for roles methods
    Role createRole(Role role);
    List<Role> getAllRoles();
    Role getRoleById(Long id);
    Role updateRole(Long id, Role role);
    void deleteRole(Long id);

    void assingPermissionToRoleById(Long id, Long permissionId);

}
