package az.ingress.client.mock;

import az.ingress.model.client.payment.PaymentRequestDto;
import az.ingress.model.dto.response.PaymentInfoResponse;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Profile("dev")
@RestController
@RequestMapping("/api/v1/internal/")
public class MockPaymentController {

    @PostMapping("/payments")
    public PaymentInfoResponse createPayment(@RequestBody PaymentRequestDto request) {
        return MockData.processPayment(request);
    }
}
