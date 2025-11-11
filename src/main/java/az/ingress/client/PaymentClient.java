package az.ingress.client;

import az.ingress.client.decoder.FeignErrorDecoder;
import az.ingress.model.client.payment.PaymentRequestDto;
import az.ingress.model.dto.response.PaymentInfoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "payment-service",
        url = "${payment.service.url}",
        configuration = FeignErrorDecoder.class

)
public interface PaymentClient {

    @PostMapping("/api/v1/internal/payments")
    PaymentInfoResponse createPayment(@RequestBody PaymentRequestDto request);
}