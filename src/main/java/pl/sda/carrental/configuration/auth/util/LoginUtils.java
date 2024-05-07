package pl.sda.carrental.configuration.auth.util;

import pl.sda.carrental.configuration.auth.model.User;
import pl.sda.carrental.configuration.auth.repository.UserRepository;
import pl.sda.carrental.exceptionHandling.ObjectAlreadyExistsException;

import java.util.Optional;

public class LoginUtils {

    public static void checkDuplicateLogin(String login, UserRepository userRepository) {
        Optional<String> duplicate = userRepository.findAll().stream()
                .map(User::getLogin)
                .filter(existingLogin -> existingLogin.equals(login))
                .findFirst();
        if (duplicate.isPresent()) {
            throw new ObjectAlreadyExistsException("Login <" + duplicate.get() + "> already exists!");
        }
    }
}
