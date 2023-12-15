package pl.sda.carrental.exceptionHandling;

public class ObjectNotFoundInRepositoryException extends RuntimeException {
    public ObjectNotFoundInRepositoryException(String message) {
        super(message);
    }
}
