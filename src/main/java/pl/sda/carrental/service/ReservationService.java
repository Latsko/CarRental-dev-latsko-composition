package pl.sda.carrental.service;

//import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sda.carrental.ObjectNotFoundInRepositoryException;
import pl.sda.carrental.model.*;
import pl.sda.carrental.repository.BranchRepository;
import pl.sda.carrental.repository.CarRepository;
import pl.sda.carrental.repository.ClientRepository;
import pl.sda.carrental.repository.ReservationRepository;


import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final BranchRepository branchRepository;
    private final CarRepository carRepository;
    private final ClientRepository clientRepository;

    @Transactional
    public ReservationModel saveReservation(ReservationDTO reservationDto) {
        ReservationModel reservation = new ReservationModel();
        updateReservationDetails(reservationDto, reservation);
        return reservationRepository.save(reservation);
    }

    @Transactional
    public ReservationModel editReservation(Long id, ReservationDTO reservationDTO) {
        ReservationModel foundReservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundInRepositoryException("No reservation under ID #" + id));
        updateReservationDetails(reservationDTO, foundReservation);
        return reservationRepository.save(foundReservation);
    }

    private void updateReservationDetails(ReservationDTO reservationDto, ReservationModel reservation) {
        setStartEndBranch(reservationDto, reservation);
        reservation.setStartDate(reservationDto.startDate());
        reservation.setEndDate(reservationDto.endDate());

        CarModel carFromRepo = carRepository.findById(reservationDto.car_id())
                .orElseThrow(() -> new ObjectNotFoundInRepositoryException("No car under that ID"));
        reservation.setCar(carFromRepo);

        ClientModel clientFromRepo = clientRepository.findById(reservationDto.customer_id())
                .orElseThrow(() -> new ObjectNotFoundInRepositoryException("No customer under that ID"));
        reservation.setCustomer(clientFromRepo);

        long daysDifference = ChronoUnit.DAYS.between(reservation.getStartDate(), reservation.getEndDate());
        BigDecimal price = carFromRepo.getPrice().multiply(BigDecimal.valueOf(daysDifference));
        reservation.setPrice(price);
    }

    private void setStartEndBranch(ReservationDTO reservationDto, ReservationModel reservation) {
        BranchModel startBranch = branchRepository.findById(reservationDto.startBranchId())
                .orElseThrow(() -> new ObjectNotFoundInRepositoryException("Branch not found"));
        reservation.setStartBranch(startBranch);
        BranchModel endBranch = branchRepository.findById(reservationDto.endBranchId())
                .orElseThrow(() -> new ObjectNotFoundInRepositoryException("Branch not found"));
        reservation.setEndBranch(endBranch);
    }

    public List<ReservationModel> getAllReservations() {
        return reservationRepository.findAll();
    }

    @Transactional
    public void deleteReservationById(Long id) {
        ReservationModel reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundInRepositoryException("No reservation under ID #" + id));
        reservationRepository.delete(reservation);
    }
}
