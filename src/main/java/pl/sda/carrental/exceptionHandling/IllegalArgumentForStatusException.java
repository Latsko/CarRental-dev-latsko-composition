package pl.sda.carrental.exceptionHandling;

public class IllegalArgumentForStatusException extends IllegalArgumentException{
    public IllegalArgumentForStatusException(String s) {
        super(s);
    }
}
