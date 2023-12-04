package pl.sda.carrental;

public class ReturnAlreadyExistsForReservation extends RuntimeException {
    public ReturnAlreadyExistsForReservation(String message) {
        super(message);
    }
}
