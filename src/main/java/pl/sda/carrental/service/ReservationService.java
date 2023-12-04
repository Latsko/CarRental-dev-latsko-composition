package pl.sda.carrental.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.sda.carrental.ObjectNotFoundInRepositoryException;
import pl.sda.carrental.model.*;
import pl.sda.carrental.repository.BranchRepository;
import pl.sda.carrental.repository.CarRepository;
import pl.sda.carrental.repository.ClientRepository;
import pl.sda.carrental.repository.ReservationRepository;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final BranchRepository branchRepository;
    private final CarRepository carRepository;
    private final ClientRepository clientRepository;

    public ReservationModel saveReservation(ReservationDTO reservationDto) {
        ReservationModel reservation = new ReservationModel();

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

        return reservationRepository.save(reservation);
    }

    private void setStartEndBranch(ReservationDTO reservationDto, ReservationModel reservation) {
        BranchModel startBranch = branchRepository.findById(reservationDto.startBranchId())
                .orElseThrow(() -> new ObjectNotFoundInRepositoryException("Branch not found"));
        reservation.setEndBranch(startBranch);
        BranchModel endBranch = branchRepository.findById(reservationDto.endBranchId())
                .orElseThrow(() -> new ObjectNotFoundInRepositoryException("Branch not found"));
        reservation.setEndBranch(endBranch);
    }

}
