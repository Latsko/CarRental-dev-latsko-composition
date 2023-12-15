package pl.sda.carrental.exceptionHandling;

public class BranchAlreadyOpenInCityException extends RuntimeException{
    public BranchAlreadyOpenInCityException(String message) {
        super(message);
    }
}
