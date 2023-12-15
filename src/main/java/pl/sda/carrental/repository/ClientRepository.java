package pl.sda.carrental.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.sda.carrental.model.Client;

public interface ClientRepository extends JpaRepository<Client, Long> {

}
