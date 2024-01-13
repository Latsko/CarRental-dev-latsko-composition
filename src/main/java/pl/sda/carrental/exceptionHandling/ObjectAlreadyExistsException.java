package pl.sda.carrental.exceptionHandling;

public class ObjectAlreadyExistsException extends RuntimeException{
    public ObjectAlreadyExistsException(String message) {
        super(message);
    }
}
