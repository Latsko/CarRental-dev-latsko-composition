package pl.sda.carrental.configuration.security.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.sda.carrental.configuration.security.entity.Role;
import pl.sda.carrental.configuration.security.entity.User;
import pl.sda.carrental.configuration.security.repository.RoleRepository;
import pl.sda.carrental.configuration.security.repository.UserRepository;
import pl.sda.carrental.configuration.security.service.UserService;

import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public void saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Role role = roleRepository.findByName("ROLE_ADMIN");
        if(role == null) {
            role = checkIfRoleExists();
        }
        user.setRoles(List.of(role));
        userRepository.save(user);
    }

    @Override
    public User findUserByName(String name) {
        return userRepository.findByName(name);
    }

    private Role checkIfRoleExists() {
        Role role = new Role();
        role.setName("ROLE_ADMIN");
        return roleRepository.save(role);
    }
}
