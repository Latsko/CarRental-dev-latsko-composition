package pl.sda.carrental.configuration.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.sda.carrental.configuration.auth.entity.Client;

public interface ClientRepository extends JpaRepository<Client, Long> {

}
