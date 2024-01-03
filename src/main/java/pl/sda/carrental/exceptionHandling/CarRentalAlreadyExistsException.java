package pl.sda.carrental.exceptionHandling;

public class CarRentalAlreadyExistsException extends RuntimeException{
    public CarRentalAlreadyExistsException(String message) {
        super(message);
    }
}
