package az.ingress.model.client.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductItemValidateRequestDto {
    private Long productId;
    private Long productVariantId;
    private int quantity;
}
