package pl.sda.carrental.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.sda.carrental.model.Branch;

public interface BranchRepository extends JpaRepository<Branch, Long> {

}
