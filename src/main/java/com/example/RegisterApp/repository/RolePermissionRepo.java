package com.example.RegisterApp.repository;

import com.example.RegisterApp.model.RolePermissions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolePermissionRepo extends JpaRepository<RolePermissions, Long> {
    // Custom query methods can be defined here if needed

}
