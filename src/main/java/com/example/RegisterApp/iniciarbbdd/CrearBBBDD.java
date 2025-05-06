package com.example.RegisterApp.iniciarbbdd;




import com.example.RegisterApp.model.*;
import com.example.RegisterApp.repository.PermissionRepository;
import com.example.RegisterApp.repository.RolePermissionRepo;
import com.example.RegisterApp.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import com.example.RegisterApp.repository.RoleRepository;
import com.example.RegisterApp.repository.UserRoleRepository;
import com.example.RegisterApp.services.UserService;

@Component
public class CrearBBBDD implements CommandLineRunner {
    @Autowired
    UserRoleRepository userRoleRepository;
    @Autowired
    UserService userService;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    PermissionRepository permissionRepository;
    @Autowired
    RolePermissionRepo rolePermissionRepository;
    @Autowired
    JwtUtil jwtUtil;
    @Override
    public void run(String... args) throws Exception {
        if(roleRepository.findAll().isEmpty()){
            //Create permissions
            Permissions permission1 = new Permissions();
            permission1.setName("READ");
            permission1.setDescription("Permiso de lectura");


            Permissions permission2 = new Permissions();
            permission2.setName("WRITE");
            permission2.setDescription("Permiso de escritura");

            //save perms
            permissionRepository.save(permission1);
            permissionRepository.save(permission2);
            // Create roles

            Role role = new Role();
            role.setName("ROLE_ADMIN");
            role.setDescription("Administrador");
            roleRepository.save(role);

            Role role2 = new Role();
            role2.setName("ROLE_USER");
            role2.setDescription("Usuario");
            roleRepository.save(role2);

            // Create users
            User admin =new User();
            admin.setName("admin");
            admin.setLastName("");
            admin.setUsername("admin");
            admin.setEmail("admin@gmail.com");
            admin.setPassword("1234");
            userService.saveCifrandoPassword(admin);


            User user=new User();
            user.setName("Pablo");
            user.setLastName("Alvarez");
            user.setUsername("pabloalvarez");
            user.setEmail("pabloalvarezolaya@gmail.com");
            user.setPassword("1234");
            user.setCreated_by(admin);
            userService.saveCifrandoPassword(user);

            User user2=new User();
            user2.setName("Juan");
            user2.setLastName("Perez");
            user2.setUsername("juanperez");
            user2.setEmail("juan@gmail.com");
            user2.setPassword("1234");
            user2.setCreated_by(admin);
            userService.saveCifrandoPassword(user2);

            //Le asigno rol de ADMIN
            UserRole userRole = new UserRole();
            userRole.setRole(role); //role es el de admin
            userRole.setUser(admin);
            userRoleRepository.save(userRole);

            UserRole userRole2 = new UserRole();
            userRole2.setRole(role2); //role2 es el de usuario
            userRole2.setUser(user);
            userRoleRepository.save(userRole2);

            UserRole userRole3 = new UserRole();
            userRole3.setRole(role2); //role2 es el de usuario
            userRole3.setUser(user2);
            userRoleRepository.save(userRole3);

            // Assign permissions to roles
            RolePermissions rolePermission = new RolePermissions();
            rolePermission.setRole(role); //role es el de admin
            rolePermission.setPermission(permission1); //permission1 es el de lectura
            rolePermissionRepository.save(rolePermission);

            // Generate and print tokens
            String adminToken = jwtUtil.generateToken(userService.loadUserByUsername(admin.getEmail()));
            String userToken = jwtUtil.generateToken(userService.loadUserByUsername(user.getEmail()));
            String user2Token = jwtUtil.generateToken(userService.loadUserByUsername(user2.getEmail()));

            System.out.println("Admin Token: " + adminToken);
            System.out.println("User Token: " + userToken);
            System.out.println("User2 Token: " + user2Token);
        }

    }
}
