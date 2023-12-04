package pl.sda.carrental.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.sda.carrental.model.ClientModel;

public interface ClientRepository extends JpaRepository<ClientModel, Long> {

}
