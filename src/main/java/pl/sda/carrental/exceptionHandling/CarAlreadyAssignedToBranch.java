package pl.sda.carrental.exceptionHandling;

public class CarAlreadyAssignedToBranch extends RuntimeException{
    public CarAlreadyAssignedToBranch(String message) {
        super(message);
    }
}
