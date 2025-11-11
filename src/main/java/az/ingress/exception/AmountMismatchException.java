package az.ingress.exception;

import lombok.Getter;

@Getter
public class AmountMismatchException extends RuntimeException {
    private String code;

    public AmountMismatchException(String code, String message) {
        super(message);
        this.code = code;
    }
}
