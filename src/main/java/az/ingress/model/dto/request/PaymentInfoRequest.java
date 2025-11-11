package az.ingress.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentInfoRequest {
    private BigDecimal amount;
    private String currency;
    private String paymentMethod;
    private String cardHolderName;
    private String description;
}
