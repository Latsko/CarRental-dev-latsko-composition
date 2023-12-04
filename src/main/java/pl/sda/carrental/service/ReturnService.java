package pl.sda.carrental.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.sda.carrental.ObjectNotFoundInRepositoryException;
import pl.sda.carrental.ReturnAlreadyExistsForReservation;
import pl.sda.carrental.model.*;
import pl.sda.carrental.repository.ReservationRepository;
import pl.sda.carrental.repository.ReturnRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReturnService {
    private final ReservationRepository reservationRepository;
    private final ReturnRepository returnRepository;

    public ReturnModel saveReturn(ReturnDTO returnDTO) {
        List<Object[]> reservationsIds = returnRepository.findReturnsWithReservationId(returnDTO.reservationId());
        if(!reservationsIds.isEmpty()) {
            throw new ReturnAlreadyExistsForReservation("Return already exists for reservation with id " + returnDTO.reservationId());
        }

        ReturnModel returnToSave = new ReturnModel();
        returnToSave.setEmployee(returnDTO.employee());
        returnToSave.setReturnDate(returnDTO.returnDate());
        returnToSave.setComments(returnDTO.comments());
        returnToSave.setUpcharge(returnDTO.upcharge());

        ReservationModel reservationFromRepository = reservationRepository.findById(returnDTO.reservationId())
                .orElseThrow(() -> new ObjectNotFoundInRepositoryException("Reservation with id "
                        + returnDTO.reservationId() + " not found"));

        returnToSave.setReservation(reservationFromRepository);

        return returnRepository.save(returnToSave);
    }
}
