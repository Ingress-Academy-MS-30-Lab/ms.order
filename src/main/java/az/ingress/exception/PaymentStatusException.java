package az.ingress.exception;

import lombok.Getter;

@Getter
public class PaymentStatusException extends RuntimeException {
    private String code;

    public PaymentStatusException(String code, String message) {
        super(message);
        this.code = code;
    }
}
