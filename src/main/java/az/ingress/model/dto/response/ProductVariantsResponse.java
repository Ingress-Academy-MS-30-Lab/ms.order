package az.ingress.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductVariantsResponse {
    private Long productVariantId;
    private String imageUrl;
    private BigDecimal price;
    private boolean onSale;
    private BigDecimal salePrice;
    private int requestedQuantity;
}