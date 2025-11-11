package az.ingress.model.dto.response;

import az.ingress.model.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductReservationResponse {

    private Long reservationId;
    private String reason;
    private OrderStatus status;
    private LocalDateTime reservationExpiresAt;
    private List<ProductIssueResponse> issues;
}
