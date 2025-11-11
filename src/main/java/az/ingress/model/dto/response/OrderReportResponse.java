package az.ingress.model.dto.response;

import az.ingress.model.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderReportResponse {
    private String orderNumber;
    private LocalDateTime createdAt;
    private OrderStatus status;
    private List<OrderItemReportResponse> orderItems;
    private BigDecimal totalAmount;
}
