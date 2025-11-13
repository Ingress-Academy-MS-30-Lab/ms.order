package az.ingress.model.client.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductItemReservationRequestDto {
    private Long productId;
    private List<Long> productVariants;
    private int quantity;
}
