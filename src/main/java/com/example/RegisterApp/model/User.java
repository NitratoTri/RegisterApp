package com.example.RegisterApp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.annotation.Priority;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name="users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @Column(nullable=false, unique=true)
    private String username;
    @Column(nullable=false)
    private String password;
    @Column(nullable=false, unique=true)
    private String email;
    @Column(nullable=false)
    private String name;
    @Column(nullable=false)
    private String lastName;
    //Acitvo o no
    //@JsonIgnore
    //We make this field false by default
    private boolean status= false;
    @JsonIgnore
    private Date created_date= Date.valueOf(LocalDate.now());
    @JsonIgnore
    private Date updated_date;
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "created_by_id")
    private User created_by;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "updated_by_id")
    private User updated_by;
    //Relation between user and role
    //Exclude it from the JSON serialization
    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserRole> userRoles = new ArrayList<>();


    public User() {
    }

    public User(String username, String password, String email, String name, String lastName) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.name = name;
        this.lastName = lastName;
    }

   public User(String username, String password, String email, String name, String lastName, boolean status, Date created_date, Date updated_date, User created_by, User updated_by) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.name = name;
        this.lastName = lastName;
        this.status = status;
        this.created_date = created_date;
        this.updated_date = updated_date;
        this.created_by = created_by;
        this.updated_by = updated_by;
    }

    public void setUserRoles(Role role) {
        this.userRoles.add(new UserRole(this, role));
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
