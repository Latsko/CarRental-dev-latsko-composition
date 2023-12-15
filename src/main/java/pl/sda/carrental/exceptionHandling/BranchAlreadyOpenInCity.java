package pl.sda.carrental.exceptionHandling;

public class BranchAlreadyOpenInCity extends RuntimeException{
    public BranchAlreadyOpenInCity(String message) {
        super(message);
    }
}
