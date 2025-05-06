package com.example.RegisterApp.repository;

import com.example.RegisterApp.model.Permissions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends JpaRepository<Permissions, Long> {
    // Custom query methods can be defined here if needed

}
