package az.ingress.model.client.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductValidateRequestDto {
    private Long userId;
    private List<ProductItemValidateRequestDto> products;
}
