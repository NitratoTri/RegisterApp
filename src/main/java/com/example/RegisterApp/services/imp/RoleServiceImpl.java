package com.example.RegisterApp.services.imp;

import com.example.RegisterApp.model.Role;
import com.example.RegisterApp.repository.RoleRepository;
import com.example.RegisterApp.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    RoleRepository roleRepository;

    //Service for roles methods
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role createRole(Role role) {
        Role newRole = new Role();
        newRole.setName(role.getName());
        newRole.setDescription(role.getDescription());

        return roleRepository.save(newRole);
    }

    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }
    public Role getRoleById(Long id) {
        return roleRepository.findById(id).orElse(null);
    }

    public Role updateRole(Long id, Role role) {
        Role existingRole = roleRepository.findById(id).orElse(null);
        if (existingRole != null) {
            existingRole.setName(role.getName());
            existingRole.setDescription(role.getDescription());
            System.out.println("Role updated successfully");
            return roleRepository.save(existingRole);

        }
        System.out.println("Role not found with id: " + id);
        return null;
    }

    public void deleteRole(Long id) {
        roleRepository.deleteById(id);
    }
}
