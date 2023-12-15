package pl.sda.carrental.exceptionHandling;

public class ReturnAlreadyExistsForReservation extends RuntimeException {
    public ReturnAlreadyExistsForReservation(String message) {
        super(message);
    }
}
