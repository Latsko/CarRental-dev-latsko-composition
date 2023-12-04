package pl.sda.carrental.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.sda.carrental.model.ReturnModel;

import java.util.List;

public interface ReturnRepository extends JpaRepository<ReturnModel, Long> {
    @Query("SELECT res.reservation_id FROM ReturnModel r JOIN r.reservation res WHERE res.reservation_id = :reservationId")
    List<Object[]> findReturnsWithReservationId(Long reservationId);
}
