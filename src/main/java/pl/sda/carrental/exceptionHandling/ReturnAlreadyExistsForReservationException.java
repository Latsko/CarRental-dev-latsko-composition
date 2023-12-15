package pl.sda.carrental.exceptionHandling;

public class ReturnAlreadyExistsForReservationException extends RuntimeException {
    public ReturnAlreadyExistsForReservationException(String message) {
        super(message);
    }
}
