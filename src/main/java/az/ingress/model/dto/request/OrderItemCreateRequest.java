package az.ingress.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemCreateRequest {

    private Long productId;

    private List<OrderItemVariantRequest> productVariants;

    private Integer quantity;

}