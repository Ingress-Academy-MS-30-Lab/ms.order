package az.ingress.exception;

import az.ingress.logger.ApplicationLogger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static az.ingress.exception.ExceptionConstants.CLIENT_ERROR;
import static az.ingress.exception.ExceptionConstants.UNEXPECTED_ERROR;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.METHOD_NOT_ALLOWED;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestControllerAdvice
public class ErrorHandler {
    private final ApplicationLogger log = ApplicationLogger.getLogger(ErrorHandler.class);

    @ExceptionHandler(Exception.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public ErrorResponse handle(Exception ex) {
        log.error("Exception: ", ex);
        return new ErrorResponse(UNEXPECTED_ERROR.getCode(), UNEXPECTED_ERROR.getMessage());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(METHOD_NOT_ALLOWED)
    public ErrorResponse handle(HttpRequestMethodNotSupportedException ex) {
        log.error("HttpRequestMethodNotSupportedException: ", ex);
        return new ErrorResponse("HTTP_REQUEST_METHOD_NOT_SUPPORTED_EXCEPTION", ex.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public ErrorResponse handle(NotFoundException ex) {
        log.error("Not found exception: " + ex);
        return new ErrorResponse(ex.getCode(), ex.getMessage());
    }

    @ExceptionHandler(AlreadyExistsException.class)
    @ResponseStatus(CONFLICT)
    public ErrorResponse handle(AlreadyExistsException ex) {
        log.error("Already exception: ", ex);
        return new ErrorResponse(ex.getCode(), ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handle(IllegalArgumentException ex) {
        log.error("IllegalArgumentException: ", ex);
        return new ErrorResponse("INVALID_ARGUMENT", ex.getMessage());
    }

    @ExceptionHandler(AmountMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handle(AmountMismatchException ex) {
        log.error("Amount mismatch exception: ", ex);
        return new ErrorResponse(ExceptionConstants.AMOUNT_MISMATCH.getCode(), ex.getMessage());
    }

    @ExceptionHandler(PaymentStatusException.class)
    @ResponseStatus(HttpStatus.PAYMENT_REQUIRED)
    public ErrorResponse handle(PaymentStatusException ex) {
        log.error("PaymentStatusException: ", ex);
        return new ErrorResponse("PAYMENT_REQUIRED", ex.getMessage());
    }

    @ExceptionHandler(CustomFeignException.class)
    public ResponseEntity<ErrorResponse> handle(CustomFeignException ex) {
        log.error("CustomFeignException: ", ex);
        return ResponseEntity.status(ex.getStatus()).body(
                new ErrorResponse(CLIENT_ERROR.getCode(),
                        CLIENT_ERROR.getMessage())
        );
    }


}