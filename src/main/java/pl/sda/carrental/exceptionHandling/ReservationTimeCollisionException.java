package pl.sda.carrental.exceptionHandling;

public class ReservationTimeCollisionException extends RuntimeException {
    public ReservationTimeCollisionException(String message) {
        super(message);
    }
}
