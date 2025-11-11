package az.ingress.model.dto.response;

import az.ingress.model.enums.PaymentErrorType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PaymentIssueResponse {
    PaymentErrorType issueType;
    String message;
}
