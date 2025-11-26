package az.ingress.service.abstraction;

import az.ingress.dao.entity.OrderEntity;
import az.ingress.dao.entity.PaymentInfoEntity;
import az.ingress.model.dto.request.OrderConfirmRequest;
import az.ingress.model.dto.response.OrderResponse;
import az.ingress.model.dto.response.PaymentInfoResponse;

import java.util.List;

public interface PaymentHandleService {
    OrderResponse handlePaymentRequest(OrderEntity entity);

    void updateAndPayment(PaymentInfoEntity payment, OrderConfirmRequest request);

    OrderResponse orderCompleted(PaymentInfoResponse response, OrderEntity entity, List<String> categories);

    OrderResponse handlePaymentFailure(PaymentInfoResponse response, OrderEntity entity, List<String> categories);
}
