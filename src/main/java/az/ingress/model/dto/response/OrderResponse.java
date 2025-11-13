package az.ingress.model.dto.response;

import az.ingress.model.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class OrderResponse {
    private Long orderId;
    private Long userId;
    private boolean isSuccess;
    private OrderStatus status;
    private BigDecimal totalPrice;
    private String message;
    private List<ProductIssueResponse> productIssues;
    private List<PaymentIssueResponse> paymentIssues;
}
