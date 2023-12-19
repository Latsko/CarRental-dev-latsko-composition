package pl.sda.carrental.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sda.carrental.exceptionHandling.ObjectNotFoundInRepositoryException;
import pl.sda.carrental.exceptionHandling.ReturnAlreadyExistsForReservationException;
import pl.sda.carrental.model.Employee;
import pl.sda.carrental.model.Reservation;
import pl.sda.carrental.model.DTO.ReturnDTO;
import pl.sda.carrental.model.Returnal;
import pl.sda.carrental.repository.EmployeeRepository;
import pl.sda.carrental.repository.ReservationRepository;
import pl.sda.carrental.repository.ReturnRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReturnService {
    private final ReservationRepository reservationRepository;
    private final ReturnRepository returnRepository;
    private final EmployeeRepository employeeRepository;

    public List<Returnal> getAllReturnals() {
        return returnRepository.findAll();
    }

    @Transactional
    public Returnal saveReturn(ReturnDTO returnDTO) {
        List<Long> reservationsIds = returnRepository.findReturnsWithReservationId(returnDTO.reservationId());
        if(!reservationsIds.isEmpty()) {
            throw new ReturnAlreadyExistsForReservationException("Return already exists for reservation with id " + returnDTO.reservationId());
        }

        Returnal returnal = new Returnal();
        updateReturnalDetails(returnDTO, returnal);

        return returnRepository.save(returnal);
    }

    @Transactional
    public Returnal editReturnal(Long id, ReturnDTO returnDTO) {
        Returnal returnal = returnRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundInRepositoryException("No returnal under ID #" + id));
        updateReturnalDetails(returnDTO, returnal);

        return returnRepository.save(returnal);
    }

    private void updateReturnalDetails(ReturnDTO returnDTO, Returnal returnalToSave) {

        returnalToSave.setReturnDate(returnDTO.returnDate());
        returnalToSave.setComments(returnDTO.comments());
        returnalToSave.setUpcharge(returnDTO.upcharge());

        Employee employeeFromRepository = employeeRepository.findById(returnDTO.employee())
                        .orElseThrow(() -> new ObjectNotFoundInRepositoryException("No employee under ID #" + returnDTO.employee()));

        returnalToSave.setEmployee(employeeFromRepository);

        Reservation reservationFromRepository = reservationRepository.findById(returnDTO.reservationId())
                .orElseThrow(() -> new ObjectNotFoundInRepositoryException("Reservation with id "
                        + returnDTO.reservationId() + " not found"));

        returnalToSave.setReservation(reservationFromRepository);
    }

    @Transactional
    public void deleteReturnal(Long id) {
        returnRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundInRepositoryException("No returnal under ID #" + id));
        returnRepository.deleteById(id);
    }
}
