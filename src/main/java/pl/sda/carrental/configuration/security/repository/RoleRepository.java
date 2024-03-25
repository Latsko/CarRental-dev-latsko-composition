package pl.sda.carrental.configuration.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.sda.carrental.configuration.security.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}
