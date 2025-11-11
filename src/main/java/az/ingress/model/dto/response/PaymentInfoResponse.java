package az.ingress.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PaymentInfoResponse {

    private boolean isSuccess;

    private String reason;

    private Long orderId;

    private Long paymentId;

    private Long userId;

    private BigDecimal totalAmount;

    private List<PaymentIssueResponse> issues;

}
