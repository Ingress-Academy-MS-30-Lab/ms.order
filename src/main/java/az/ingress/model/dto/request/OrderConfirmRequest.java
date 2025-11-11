package az.ingress.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderConfirmRequest {

    private Long userId;
    private Long orderId;
    private PaymentInfoRequest payment;
}
