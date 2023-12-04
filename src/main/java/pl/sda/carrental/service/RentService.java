package pl.sda.carrental.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.sda.carrental.ObjectNotFoundInRepositoryException;
import pl.sda.carrental.RentAlreadyExistsForReservation;
import pl.sda.carrental.model.RentDTO;
import pl.sda.carrental.model.RentModel;
import pl.sda.carrental.model.ReservationModel;
import pl.sda.carrental.repository.RentRepository;
import pl.sda.carrental.repository.ReservationRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RentService {
    private final RentRepository rentRepository;
    private final ReservationRepository reservationRepository;

    public RentModel save(RentDTO rentDTO) {
        List<Object[]> reservationsIds = rentRepository.findRentalsWithReservationId(rentDTO.reservationId());
        if(!reservationsIds.isEmpty()) {
            throw new RentAlreadyExistsForReservation("Rent already exists for reservation with id " + rentDTO.reservationId());
        }

        RentModel rentToSave = new RentModel();
        rentToSave.setEmployee(rentDTO.employee());
        rentToSave.setComments(rentDTO.comments());
        rentToSave.setRentDate(rentDTO.rentDate());

        ReservationModel reservationFromRepository = reservationRepository.findById(rentDTO.reservationId())
                .orElseThrow(() -> new ObjectNotFoundInRepositoryException("Reservation with id "
                        + rentDTO.reservationId() + " not found"));

        rentToSave.setReservation(reservationFromRepository);

        return rentRepository.save(rentToSave);
    }
}
