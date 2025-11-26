package az.ingress.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class OrderCreateRequest {

    @NotNull
    private Long userId;

    @NotBlank
    private List<OrderItemCreateRequest> items;

    private BigDecimal totalPrice;

}