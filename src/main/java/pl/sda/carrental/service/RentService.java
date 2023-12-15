package pl.sda.carrental.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.sda.carrental.exceptionHandling.ObjectNotFoundInRepositoryException;
import pl.sda.carrental.exceptionHandling.RentAlreadyExistsForReservationException;
import pl.sda.carrental.model.Employee;
import pl.sda.carrental.model.DTO.RentDTO;
import pl.sda.carrental.model.Rent;
import pl.sda.carrental.model.Reservation;
import pl.sda.carrental.repository.EmployeeRepository;
import pl.sda.carrental.repository.RentRepository;
import pl.sda.carrental.repository.ReservationRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RentService {
    private final RentRepository rentRepository;
    private final ReservationRepository reservationRepository;
    private final EmployeeRepository employeeRepository;

    public List<Rent> allRents() {
        return rentRepository.findAll();
    }
    public Rent save(RentDTO rentDTO) {
        Rent rent = new Rent();
        updateRentDetails(rentDTO, rent);

        return rentRepository.save(rent);
    }

    public Rent editRent(Long id, RentDTO rentDTO) {
        Rent rent = rentRepository.findById(id)
                        .orElseThrow(() ->
                                new ObjectNotFoundInRepositoryException("No rent under ID #" + id));
        updateRentDetails(rentDTO, rent);

        rentRepository.deleteById(id);
        return rentRepository.save(rent);
    }

    public void deleteRentById(Long id) {
        rentRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundInRepositoryException("No rent under ID #" + id));
        rentRepository.deleteById(id);
    }

    private void updateRentDetails(RentDTO rentDTO, Rent rent) {
        List<Long> reservationsIds = rentRepository.findRentalsWithReservationId(rentDTO.reservationId());
        if(!reservationsIds.isEmpty()) {
            throw new RentAlreadyExistsForReservationException("Rent already exists for reservation with id "
                    + rentDTO.reservationId());
        }

        Employee foundEmployee = employeeRepository.findById(rentDTO.employeeId())
                .orElseThrow(() -> new ObjectNotFoundInRepositoryException("No employee under ID #"
                        + rentDTO.employeeId()));

        rent.setEmployee(foundEmployee);
        rent.setComments(rentDTO.comments());
        rent.setRentDate(rentDTO.rentDate());

        Reservation reservationFromRepository = reservationRepository.findById(rentDTO.reservationId())
                .orElseThrow(() -> new ObjectNotFoundInRepositoryException("Reservation with id "
                        + rentDTO.reservationId() + " not found"));

        rent.setReservation(reservationFromRepository);
    }
}
