package az.ingress.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemVariantRequest {

    private Long productVariantId;

    private String title;

    private BigDecimal price;

    private int requestedQuantity;

}
