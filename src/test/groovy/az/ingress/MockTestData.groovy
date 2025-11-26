package az.ingress

import az.ingress.model.dto.response.OrderResponse
import az.ingress.model.dto.response.PaymentInfoResponse

import static az.ingress.model.enums.OrderStatus.PENDING

class MockTestData {

    static PaymentInfoResponse paymentInfoResponse() {
        return new PaymentInfoResponse(
                true,
                "Success",
                1L,
                1L,
                1L,
                1250 as BigDecimal,
                null
        )
    }

    static OrderResponse orderResponse() {
        return new OrderResponse(
                1L,
                1L,
                true,
                PENDING,
                1250 as BigDecimal,
                "Success",
                null,
                null
        );
    }
}
