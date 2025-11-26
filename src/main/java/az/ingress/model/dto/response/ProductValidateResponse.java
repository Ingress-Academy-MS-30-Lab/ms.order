package az.ingress.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class ProductValidateResponse {

    private List<ProductValidateItemResponse> products;
    private BigDecimal totalPrice;

}