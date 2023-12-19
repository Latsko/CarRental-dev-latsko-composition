package pl.sda.carrental.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sda.carrental.exceptionHandling.ObjectNotFoundInRepositoryException;
import pl.sda.carrental.exceptionHandling.ReservationTimeCollisionException;
import pl.sda.carrental.model.Branch;
import pl.sda.carrental.model.Car;
import pl.sda.carrental.model.Client;
import pl.sda.carrental.model.DTO.ReservationDTO;
import pl.sda.carrental.model.Reservation;
import pl.sda.carrental.repository.BranchRepository;
import pl.sda.carrental.repository.CarRepository;
import pl.sda.carrental.repository.ClientRepository;
import pl.sda.carrental.repository.ReservationRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final BranchRepository branchRepository;
    private final CarRepository carRepository;
    private final ClientRepository clientRepository;

    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    @Transactional
    public Reservation saveReservation(ReservationDTO reservationDto) {
        Reservation reservation = new Reservation();
        updateReservationDetails(reservationDto, reservation);
        return reservationRepository.save(reservation);
    }

    @Transactional
    public Reservation editReservation(Long id, ReservationDTO reservationDTO) {
        Reservation foundReservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundInRepositoryException("No reservation under ID #" + id));
        updateReservationDetails(reservationDTO, foundReservation);
        return reservationRepository.save(foundReservation);
    }

    private void updateReservationDetails(ReservationDTO reservationDto, Reservation reservation) {
        setStartEndBranch(reservationDto, reservation);
        reservation.setStartDate(reservationDto.startDate());
        reservation.setEndDate(reservationDto.endDate());

        Car carFromRepo = carRepository.findById(reservationDto.car_id())
                .orElseThrow(() -> new ObjectNotFoundInRepositoryException("No car under that ID"));

        if (!carFromRepo.getReservations().isEmpty()) {
            List<DateTimePeriod> timeCollision = carFromRepo.getReservations().stream()
                    .map(resObject -> new DateTimePeriod(resObject.getStartDate(), resObject.getEndDate()))
                    .filter(dtp -> isDateSuitable(reservationDto, dtp))
                    .toList();
            if (!timeCollision.isEmpty()) {
                throw new ReservationTimeCollisionException("Car cannot be reserved for given time period!");
            }
        }
        reservation.setCar(carFromRepo);

        Client clientFromRepo = clientRepository.findById(reservationDto.customer_id())
                .orElseThrow(() -> new ObjectNotFoundInRepositoryException("No customer under that ID"));
        reservation.setClient(clientFromRepo);

        long daysDifference = ChronoUnit.DAYS.between(reservation.getStartDate(), reservation.getEndDate());
        BigDecimal price = carFromRepo.getPrice().multiply(BigDecimal.valueOf(daysDifference));
        reservation.setPrice(price);
    }

    private boolean isDateSuitable(ReservationDTO reservationDto, DateTimePeriod dtp) {
        return dtp.start().equals(reservationDto.startDate()) ||
                dtp.end().equals(reservationDto.endDate()) ||

                (dtp.start().isAfter(reservationDto.startDate()) &&
                        dtp.start().isBefore(reservationDto.endDate())) ||

                (dtp.end().isAfter(reservationDto.startDate()) &&
                        dtp.end().isBefore(reservationDto.endDate())) ||

                (dtp.start().isAfter(reservationDto.startDate()) &&
                        dtp.end().isBefore(reservationDto.endDate())) ||

                (dtp.start().isBefore(reservationDto.startDate()) &&
                        dtp.end().isAfter(reservationDto.endDate()));
    }

    private void setStartEndBranch(ReservationDTO reservationDto, Reservation reservation) {
        Branch startBranch = branchRepository.findById(reservationDto.startBranchId())
                .orElseThrow(() -> new ObjectNotFoundInRepositoryException("Branch not found"));
        reservation.setStartBranch(startBranch);
        Branch endBranch = branchRepository.findById(reservationDto.endBranchId())
                .orElseThrow(() -> new ObjectNotFoundInRepositoryException("Branch not found"));
        reservation.setEndBranch(endBranch);
    }

    @Transactional
    public void deleteReservationById(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundInRepositoryException("No reservation under ID #" + id));
        reservationRepository.delete(reservation);
    }
}

record DateTimePeriod(LocalDate start, LocalDate end) {
}