package az.ingress.exception;

import lombok.Getter;

@Getter
public class ReservationExpiredException extends RuntimeException {
    private String code;

    public ReservationExpiredException(String message, String code) {
        super(message);
        this.code = code;
    }
}
