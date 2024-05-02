package pl.sda.carrental.configuration.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.sda.carrental.configuration.auth.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByLogin(String login);
}
