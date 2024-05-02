package pl.sda.carrental.configuration.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.sda.carrental.configuration.auth.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}
