package az.ingress.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorMessage {
    UNEXPECTED_ERROR("UNEXPECTED_ERROR", "Unexpected error occurred"),
    ORDER_NOT_FOUND("ORDER_NOT_FOUND", "Order not found with id: %s"),
    ORDER_ITEM_NOT_FOUND("ORDER_ITEM_NOT_FOUND", "Order item not found with id: %s"),
    PAYMENT_INFO_NOT_FOUND("PAYMENT_INFO_NOT_FOUND", "Payment info not found with id: %s"),
    SHIPPING_ADDRESS_NOT_FOUND("SHIPPING_ADDRESS_NOT_FOUND", "Shipping address not found with id: %s");

    private final String code;
    private final String message;
}