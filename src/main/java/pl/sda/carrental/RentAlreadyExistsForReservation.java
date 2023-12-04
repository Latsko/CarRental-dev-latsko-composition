package pl.sda.carrental;

public class RentAlreadyExistsForReservation extends RuntimeException {
    public RentAlreadyExistsForReservation(String message) {
        super(message);
    }
}
