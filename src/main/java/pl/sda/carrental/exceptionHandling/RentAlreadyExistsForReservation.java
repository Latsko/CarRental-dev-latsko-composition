package pl.sda.carrental.exceptionHandling;

public class RentAlreadyExistsForReservation extends RuntimeException {
    public RentAlreadyExistsForReservation(String message) {
        super(message);
    }
}
