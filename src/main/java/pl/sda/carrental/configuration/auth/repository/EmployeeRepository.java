package pl.sda.carrental.configuration.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.sda.carrental.configuration.auth.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

}
