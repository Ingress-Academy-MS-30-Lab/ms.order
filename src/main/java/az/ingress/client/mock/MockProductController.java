package az.ingress.client.mock;

import az.ingress.model.client.product.ProductReservationRequestDto;
import az.ingress.model.client.product.ProductStockRequestDto;
import az.ingress.model.client.product.ProductValidateRequestDto;
import az.ingress.model.dto.response.ProductReservationResponse;
import az.ingress.model.dto.response.ProductValidateResponse;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Profile("dev")
@RestController
@RequestMapping("/api/v1/internal/")
public class MockProductController {

    @PostMapping("/product-variants/validate")
    public ProductValidateResponse getProduct(@RequestBody ProductValidateRequestDto request) {
        return MockData.productVariantsResponse(request);
    }

    @PostMapping("/products/reserve")
    public ProductReservationResponse reserveProducts(@RequestBody ProductReservationRequestDto request) {
        return MockData.productReservationResponse(request);
    }

    @PutMapping("/products/callback/stock")
    public void productStockCallback(@RequestBody ProductStockRequestDto request) {

    }

}
