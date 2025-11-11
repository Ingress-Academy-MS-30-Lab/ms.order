package az.ingress.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ExceptionConstants {
    UNEXPECTED_ERROR("UNEXPECTED_ERROR", "Unexpected error occurred"),
    CLIENT_ERROR("CLIENT_ERROR", "Exception from client"),
    RESERVATION_EXPIRED_EXCEPTION("RESERVATION_EXPIRED_EXCEPTION", "Reservation expired exception"),
    ORDER_NOT_FOUND("ORDER_NOT_FOUND", "Order not found with id: %s"),
    AMOUNT_MISMATCH("AMOUNT_MISMATCH", "Amounts do not match! Expected: %s, Actual: %s"),
    ORDER_ITEM_NOT_FOUND("ORDER_ITEM_NOT_FOUND", "Order item not found with id: %s"),
    PAYMENT_INFO_NOT_FOUND("PAYMENT_INFO_NOT_FOUND", "Payment info not found with id: %s"),
    SHIPPING_ADDRESS_NOT_FOUND("SHIPPING_ADDRESS_NOT_FOUND", "Shipping address not found with id: %s"),
    EVENT_ALREADY_EXISTS("EVENT_ALREADY_EXISTS", "Event already exists with id: %s");

    private final String code;
    private final String message;

    public String format(Object... args) {
        return String.format(message, args);
    }
}