package pl.sda.carrental.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.sda.carrental.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

}
