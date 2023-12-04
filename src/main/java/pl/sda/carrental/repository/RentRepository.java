package pl.sda.carrental.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.sda.carrental.model.RentModel;

import java.util.List;

@Repository
public interface RentRepository extends JpaRepository<RentModel, Long> {

    @Query("SELECT res.reservation_id FROM RentModel r JOIN r.reservation res WHERE res.reservation_id = :reservationId")
    List<Object[]> findRentalsWithReservationId(Long reservationId);
}
