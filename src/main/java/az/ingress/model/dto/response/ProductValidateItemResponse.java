package az.ingress.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductValidateItemResponse {
    private String category;
    private Long productId;
    private String title;
    private List<ProductVariantsResponse> productVariants;
    private int quantity;
}