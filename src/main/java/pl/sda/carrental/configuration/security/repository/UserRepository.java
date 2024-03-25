package pl.sda.carrental.configuration.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.sda.carrental.configuration.security.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByName(String name);
}
