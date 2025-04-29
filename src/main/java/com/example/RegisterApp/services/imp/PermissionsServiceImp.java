package com.example.RegisterApp.services.imp;

import com.example.RegisterApp.model.Permissions;
import com.example.RegisterApp.repository.PermissionRepository;
import com.example.RegisterApp.services.PermissionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermissionsServiceImp implements PermissionsService {
    // Implement the methods defined in PermissionsService interface here
    @Autowired
    PermissionRepository permissionRepository;

    public void createPermission(Permissions per) {
        // Implementation for creating a permission
        Permissions newPermission = new Permissions();
        newPermission.setName(per.getName());
        newPermission.setDescription(per.getDescription());
        //newPermission.setRole(per.getRole());
        permissionRepository.save(newPermission);
        System.out.println("Permission created successfully");
    }

    public Permissions getPermissionById(Long id) {
        // Implementation for getting a permission by ID
        return permissionRepository.findById(id).orElse(null);
    }
    public void updatePermission(Long id, Permissions per) {
        // Implementation for updating a permission
        Permissions existingPermission = permissionRepository.findById(id).orElse(null);
        if (existingPermission != null) {
            existingPermission.setName(per.getName());
            existingPermission.setDescription(per.getDescription());
            permissionRepository.save(existingPermission);
            System.out.println("Permission updated successfully");
        } else {
            System.out.println("Permission not found with id: " + id);
        }
    }

    public void deletePermission(Long id) {
        // Implementation for deleting a permission
        permissionRepository.deleteById(id);
        System.out.println("Permission deleted successfully");
    }
    public List<Permissions> getAllPermissions() {
        // Implementation for getting all permissions
        return permissionRepository.findAll();
    }

}
