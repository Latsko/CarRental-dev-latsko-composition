package pl.sda.carrental.exceptionHandling;

public class RentAlreadyExistsForReservationException extends RuntimeException {
    public RentAlreadyExistsForReservationException(String message) {
        super(message);
    }
}
