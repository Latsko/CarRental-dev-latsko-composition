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
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final BranchRepository branchRepository;
    private final CarRepository carRepository;
    private final ClientRepository clientRepository;

    public ReservationModel saveReservation(ReservationDTO reservationDto) {
        // cannot create ReservationModel with no parameters!
        // fields are validated in model as not null
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
        reservation.setStartBranch(startBranch);
        BranchModel endBranch = branchRepository.findById(reservationDto.endBranchId())
                .orElseThrow(() -> new ObjectNotFoundInRepositoryException("Branch not found"));
        reservation.setEndBranch(endBranch);
    }

    public List<ReservationModel> getAllReservations() {
        return reservationRepository.findAll();
    }

    public ReservationModel editReservation(Long id, ReservationDTO reservationDTO) {
        ReservationModel foundReservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundInRepositoryException("No reservation under ID #" + id));

        // major part of this code is repeated in the saveReservation() method
        // have to consider extracting block of code to a separate method

        setStartEndBranch(reservationDTO, foundReservation);
        foundReservation.setStartDate(reservationDTO.startDate());
        foundReservation.setEndDate(reservationDTO.endDate());

        CarModel carFromRepo = carRepository.findById(reservationDTO.car_id())
                .orElseThrow(() -> new ObjectNotFoundInRepositoryException("No car under that ID"));
        foundReservation.setCar(carFromRepo);

        ClientModel clientFromRepo = clientRepository.findById(reservationDTO.customer_id())
                .orElseThrow(() -> new ObjectNotFoundInRepositoryException("No customer under that ID"));
        foundReservation.setCustomer(clientFromRepo);

        long daysDifference = ChronoUnit.DAYS.between(foundReservation.getStartDate(), foundReservation.getEndDate());
        BigDecimal price = carFromRepo.getPrice().multiply(BigDecimal.valueOf(daysDifference));
        foundReservation.setPrice(price);

        reservationRepository.deleteById(id);
        return reservationRepository.save(foundReservation);
    }

    public void deleteReservationById(Long id) {
        reservationRepository.findById(id)
                        .orElseThrow(() ->
                                new ObjectNotFoundInRepositoryException("No reservation under ID #" + id));
        reservationRepository.deleteById(id);
    }
}
