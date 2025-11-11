package az.ingress.model.client.product;

import az.ingress.model.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductStockRequestDto {

    private Long orderId;

    private Long userId;

    private OrderStatus status;

    private String reason;
}
