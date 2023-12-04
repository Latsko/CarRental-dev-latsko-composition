package pl.sda.carrental.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.sda.carrental.model.RevenueModel;

@Repository
public interface RevenueRepository extends JpaRepository<RevenueModel, Long> {
}
