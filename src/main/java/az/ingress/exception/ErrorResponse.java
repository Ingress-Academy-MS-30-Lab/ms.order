package az.ingress.exception;

public record ErrorResponse(
        String code,
        String message) {

}