package pl.sda.carrental.configuration.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.sda.carrental.configuration.security.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByLogin(String name);
}
