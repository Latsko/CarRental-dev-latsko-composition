package pl.sda.carrental.configuration.security.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.sda.carrental.configuration.security.dto.UserDto;
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
    public UserDto saveUser(UserDto userDto) {
        User user = new User();
        user.setName(userDto.getName());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        Role role = roleRepository.findByName("ROLE_ADMIN");
        if(role == null) {
            role = checkIfRoleExists();
        }
        user.setRoles(List.of(role));
        return mapToUserDto(userRepository.save(user));
    }

    @Override
    public UserDto findUserByName(String name) {
        return mapToUserDto(userRepository.findByName(name));
    }

    @Override
    public List<UserDto> findAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(this::mapToUserDto)
                .toList();
    }

    private Role checkIfRoleExists() {
        Role role = new Role();
        role.setName("ROLE_ADMIN");
        return roleRepository.save(role);
    }

    private UserDto mapToUserDto(User user) {
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getPassword());
    }
}
