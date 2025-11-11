package az.ingress.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderItemReportResponse {
    private String productName;
    private String imageUrl;
    private BigDecimal productPrice;
    private BigDecimal salePrice;
    private int requestedQuantity;
}