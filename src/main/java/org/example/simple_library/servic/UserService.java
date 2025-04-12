package org.example.simple_library.servic;

import org.example.simple_library.dto.UserDto;
import org.example.simple_library.entity.Role;
import org.example.simple_library.entity.RoleName;
import org.example.simple_library.entity.Users;
import org.example.simple_library.repo.RoleRepository;
import org.example.simple_library.repo.UsersRepository;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UsersRepository usersRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    public UserService(UsersRepository usersRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.usersRepository = usersRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public HttpEntity<?> register(UserDto userdto) {
        Users user = new Users();
        user.setUsername(userdto.getUsername());
        user.setPassword(passwordEncoder.encode(userdto.getPassword()));

        Role role = roleRepository.findByRoleName(RoleName.ROLE_USER);
        user.setRole(role);

        usersRepository.save(user);
        return ResponseEntity.ok("User saved successfully");
    }
}
