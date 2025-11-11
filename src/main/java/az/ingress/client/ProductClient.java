package az.ingress.client;

import az.ingress.client.decoder.FeignErrorDecoder;
import az.ingress.model.client.product.ProductReservationRequestDto;
import az.ingress.model.client.product.ProductStockRequestDto;
import az.ingress.model.client.product.ProductValidateRequestDto;
import az.ingress.model.dto.response.ProductReservationResponse;
import az.ingress.model.dto.response.ProductValidateResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "product-service",
        url = "${product.service.url}",
        configuration = FeignErrorDecoder.class
)
public interface ProductClient {

    @PostMapping("/api/v1/internal/product-variants/validate")
    ProductValidateResponse validateProduct(@RequestBody ProductValidateRequestDto request);

    @PostMapping("/api/v1/internal/products/reserve")
    ProductReservationResponse reserveProducts(@RequestBody ProductReservationRequestDto request);

    @PutMapping("api/v1/internal/products/callback/stock")
    void productStockCallback(@RequestBody ProductStockRequestDto request);


}
