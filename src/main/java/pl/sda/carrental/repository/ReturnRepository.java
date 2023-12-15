package pl.sda.carrental.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.sda.carrental.model.Returnal;

import java.util.List;

public interface ReturnRepository extends JpaRepository<Returnal, Long> {
    @Query("SELECT res.reservationId FROM Returnal r JOIN r.reservation res WHERE res.reservationId = :reservationId")
    List<Long> findReturnsWithReservationId(Long reservationId);
}
