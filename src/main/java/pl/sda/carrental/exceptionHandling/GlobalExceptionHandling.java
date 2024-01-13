package pl.sda.carrental.exceptionHandling;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandling {

    @ExceptionHandler(ObjectNotFoundInRepositoryException.class)
    public ProblemDetail handleObjectNotFoundInRepository(ObjectNotFoundInRepositoryException exception) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, exception.getMessage());
    }

    @ExceptionHandler(ReservationTimeCollisionException.class)
    public ProblemDetail handleReservationTimeCollisionException(ReservationTimeCollisionException exception) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, exception.getMessage());
    }

    @ExceptionHandler(ObjectAlreadyAssignedToBranchException.class)
    public ProblemDetail handleClientAlreadyAssignedToBranch(ObjectAlreadyAssignedToBranchException exception) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, exception.getMessage());
    }

    @ExceptionHandler(BranchAlreadyOpenInCityException.class)
    public ProblemDetail handleBranchAlreadyOpenInCity(BranchAlreadyOpenInCityException exception) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, exception.getMessage());
    }

    @ExceptionHandler(RentAlreadyExistsForReservationException.class)
    public ProblemDetail handleRentAlreadyExistsForReservation(RentAlreadyExistsForReservationException exception) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, exception.getMessage());
    }

    @ExceptionHandler(ReturnAlreadyExistsForReservationException.class)
    public ProblemDetail handleReturnAlreadyExistsForReservation(ReturnAlreadyExistsForReservationException exception) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, exception.getMessage());
    }

    @ExceptionHandler(ObjectAlreadyExistsException.class)
    public ProblemDetail handleCarRentalAlreadyExistsException(ObjectAlreadyExistsException exception) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, exception.getMessage());
    }

    @ExceptionHandler(IllegalArgumentForStatusException.class)
    public ProblemDetail handleIllegalArgumentForStatusException(IllegalArgumentForStatusException exception) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidationException(MethodArgumentNotValidException exception) {
        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
        List<String> errors = fieldErrors.stream()
                .map(fieldError -> fieldError.getField() + " " + fieldError.getDefaultMessage())
                .toList();
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, errors.toString());
    }
}
