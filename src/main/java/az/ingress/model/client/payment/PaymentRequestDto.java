package az.ingress.model.client.payment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class PaymentRequestDto {

    private Long orderId;

    private Long userId;

    private BigDecimal amount;
    private String currency;
    private String paymentMethod;
    private String cardHolderName;
    private String description;
}
