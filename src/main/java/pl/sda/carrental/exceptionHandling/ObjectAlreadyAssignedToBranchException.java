package pl.sda.carrental.exceptionHandling;

public class ObjectAlreadyAssignedToBranchException extends RuntimeException {
    public ObjectAlreadyAssignedToBranchException(String message) {
        super(message);
    }
}
