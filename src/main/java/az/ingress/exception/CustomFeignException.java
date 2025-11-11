package az.ingress.exception;

import lombok.Getter;

@Getter
public class CustomFeignException extends RuntimeException {
    private int status;

    public CustomFeignException(int status, String message) {
        super(message);
        this.status = status;
    }
}
