package com.example.RegisterApp.services.imp;

import com.example.RegisterApp.model.Role;
import com.example.RegisterApp.model.User;
import com.example.RegisterApp.model.UserRole;
import com.example.RegisterApp.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.RegisterApp.repository.RoleRepository;
import com.example.RegisterApp.repository.UserRepository;
import com.example.RegisterApp.repository.UserRoleRepository;
import com.example.RegisterApp.services.UserService;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRoleRepository userRoleRepository;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void saveUser(UserDto userDto) {

        User user = new User();
        user.setName(userDto.getFirstName() + " " + userDto.getLastName());
        user.setEmail(userDto.getEmail());

        //encrypt the password once we integrate spring security
        //user.setPassword(userDto.getPassword());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        userRepository.save(user);

        //Después de crear el usuario, le asigno el rol usuario
        Role role = (Role) roleRepository.findByName("ROLE_USER");
        userRoleRepository.save(new UserRole(user,role));
    }

    public void save(User user) {
        userRepository.save(user);
    }

    public void saveCifrandoPassword(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    public void assignRoleToUserById(Long id, Long roleId) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found with id: " + roleId));

        UserRole userRole = new UserRole(user, role);
        userRoleRepository.save(userRole);
    }

    @Override
    public void assignRoleToUserByRoleName(Long id, String roleName) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        Role role = roleRepository.findByName(roleName);
        if (role == null) {
            throw new RuntimeException("Role not found with name: " + roleName);
        }

        UserRole userRole = new UserRole(user, role);
        userRoleRepository.save(userRole);
    }


    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<UserDto> findAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map((user) -> convertEntityToDto(user))
                .collect(Collectors.toList());
    }

    @Override
    public UserDto convertEntityToDto(User user){
        UserDto userDto = new UserDto();
        String[] name = user.getName().split(" ");
        userDto.setFirstName(name[0]);
        userDto.setLastName(name[1]);
        userDto.setEmail(user.getEmail());
        userDto.setPassword(user.getPassword());
        return userDto;
    }
    public Collection<Role> conseguirRolesByUser(User user){
        List<UserRole> userRoles = userRoleRepository.findByUser(user);
        List<Role> roles = new ArrayList<>();
        for (UserRole userRole : userRoles) {
            roles.add(userRole.getRole());
        }
        return roles;
    }

    @Override
    public User getUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    @Transactional
    public User createUser(User user) {
        User newUser = new User();
        newUser.setUsername(user.getUsername());
        newUser.setPassword(user.getPassword());
        newUser.setEmail(user.getEmail());
        newUser.setLastName(user.getLastName());
        newUser.setName(user.getName());
        //Assigming default role to the user
        Role defaultRole = roleRepository.findByName("ROLE_USER");
        if (defaultRole == null) {
            throw new RuntimeException("Default role not found");
        }
        UserRole userRole = new UserRole(newUser, defaultRole);
        userRoleRepository.save(userRole);
        return userRepository.save(newUser);
    }

    @Override
    public User updateUser(Long id, User userDetails) {
        User user = getUser(id);
        user.setUsername(userDetails.getUsername());
        user.setEmail(userDetails.getEmail());
        user.setName(userDetails.getName());
        // No actualizamos la contraseña aquí por razones de seguridad
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

   @Override
   public UserDetails loadUserByUsername(String email) {
       User user = userRepository.findByEmail(email);
       if (user == null) {
           throw new UsernameNotFoundException("User not found with email: " + email);
       }
       return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), new ArrayList<>());
   }

    @Override
    public List<String> getRolesByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + userId));
        List<UserRole> userRoles = userRoleRepository.findByUser(user);
        return userRoles.stream()
                .map(userRole -> userRole.getRole().getName())
                .collect(Collectors.toList());
    }

}
