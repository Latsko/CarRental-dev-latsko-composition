package pl.sda.carrental.configuration.security.service;

import pl.sda.carrental.configuration.security.entity.User;

public interface UserService {
    void saveUser(User user);
    User findUserByName(String name);
}
