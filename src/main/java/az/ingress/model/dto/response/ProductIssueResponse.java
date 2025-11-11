package az.ingress.model.dto.response;

import az.ingress.model.enums.ProductIssueType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductIssueResponse {
    ProductIssueType issueType;
    String message;
    Long productId;
    Long variantId;
    String title;
    int requestQuantity;
    int availableQuantity;
    BigDecimal requestedPrice;
    BigDecimal currentPrice;
}
