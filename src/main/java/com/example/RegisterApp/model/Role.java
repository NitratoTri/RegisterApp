package com.example.RegisterApp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable=false, unique=true)
    private String name;
    private String description;

    //Relation between user and role
    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<UserRole> userRoles = new ArrayList<>();

    //Relation between role and permissions
    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<RolePermissions> rolePermissions = new ArrayList<>();
    
    public Role() {
    }

    public Role(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<UserRole> getUserRoles() {
        return userRoles;
    }

    public List<RolePermissions> getRolePermissions() {
        return rolePermissions;
    }
    public void setRolePermissions(Permissions pnew) {
        RolePermissions rolePermission = new RolePermissions(this, pnew);
        this.rolePermissions.add(rolePermission);
        pnew.getRolePermissions().add(rolePermission);
    }
}
