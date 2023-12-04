package pl.sda.carrental.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.sda.carrental.model.BranchModel;

import java.util.Optional;

@Repository
public interface BranchRepository extends JpaRepository<BranchModel, Long> {
    Optional<BranchModel> findByName(String startBranchName);
}
