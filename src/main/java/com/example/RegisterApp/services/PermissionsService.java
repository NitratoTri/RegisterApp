package com.example.RegisterApp.services;

import com.example.RegisterApp.model.Permissions;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PermissionsService {
    // Define the methods for permissions service here
    void createPermission(Permissions per);
    Permissions getPermissionById(Long id);
    void updatePermission(Long id, Permissions per);
    void deletePermission(Long id);
    List<Permissions> getAllPermissions();
}
