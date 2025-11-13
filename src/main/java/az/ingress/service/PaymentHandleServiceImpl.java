package az.ingress.service;

import az.ingress.client.PaymentClient;
import az.ingress.client.ProductClient;
import az.ingress.dao.entity.OrderEntity;
import az.ingress.dao.entity.PaymentInfoEntity;
import az.ingress.dao.repository.OrderItemRepository;
import az.ingress.dao.repository.OrderRepository;
import az.ingress.dao.repository.PaymentInfoRepository;
import az.ingress.model.client.product.ProductStockRequestDto;
import az.ingress.model.dto.request.OrderConfirmRequest;
import az.ingress.model.dto.response.OrderResponse;
import az.ingress.model.dto.response.PaymentInfoResponse;
import az.ingress.model.mapper.PaymentMapper;
import az.ingress.service.abstraction.PaymentHandleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static az.ingress.model.enums.OrderStatus.PAYMENT_FAILED;
import static az.ingress.model.enums.OrderStatus.PAYMENT_PENDING;
import static az.ingress.model.enums.OrderStatus.PAYMENT_SUCCESS;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentHandleServiceImpl implements PaymentHandleService {

    private final PaymentClient paymentClient;
    private final ProductClient productClient;
    private final OrderRepository orderRepository;
    private final OutboxEventService outboxEventService;
    private final OrderItemRepository orderItemRepository;
    private final PaymentMapper paymentMapper;
    private final PaymentInfoRepository paymentInfoRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public OrderResponse handlePaymentRequest(OrderEntity entity) {
        var payment = paymentInfoRepository.findPaymentByOrder(entity);
        var paymentRequest = paymentMapper.toRequest(payment);
        paymentRequest.setOrderId(entity.getId());
        entity.setStatus(PAYMENT_PENDING);
        var paymentResponse = paymentClient.createPayment(paymentRequest);
        paymentMapper.updateEntity(payment, paymentResponse);
        paymentInfoRepository.save(payment);
        var categories = orderItemRepository.findCategoriesByOrder(entity);
        if (paymentResponse.isSuccess()) {
            log.info("Payment succeeded for orderId={} | amount={}",
                    paymentResponse.getOrderId(), paymentResponse.getTotalAmount());
            return orderCompleted(paymentResponse, entity, categories);
        } else {
            log.warn("Payment failed for orderId={} | reason={}",
                    paymentResponse.getOrderId(), paymentResponse.getReason());
            return handlePaymentFailure(paymentResponse, entity, categories);
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateAndPayment(PaymentInfoEntity payment, OrderConfirmRequest request) {
        paymentMapper.updateEntity(payment, request.getPayment());
        payment.setUserId(request.getUserId());
        paymentInfoRepository.save(payment);
    }

    public OrderResponse orderCompleted(PaymentInfoResponse response, OrderEntity entity, List<String> categories) {
        entity.setStatus(PAYMENT_SUCCESS);
        entity.setReason(response.getReason());
        orderRepository.save(entity);
        outboxEventService.saveToOutbox(entity, categories);
        productClient.productStockCallback(createProductCallBackRequest(entity));
        return createOrderCompletedResponse(entity, response);
    }

    public OrderResponse handlePaymentFailure(PaymentInfoResponse response, OrderEntity entity, List<String> categories) {
        entity.setStatus(PAYMENT_FAILED);
        entity.setReason(response.getReason());
        orderRepository.save(entity);
        outboxEventService.saveToOutbox(entity, categories);
        return createOrderFailedResponse(entity, response);
    }


    private ProductStockRequestDto createProductCallBackRequest(OrderEntity entity) {
        return new ProductStockRequestDto(
                entity.getId(),
                entity.getUserId(),
                entity.getStatus(),
                entity.getReason());
    }

    private OrderResponse createOrderCompletedResponse(OrderEntity entity, PaymentInfoResponse response) {
        return OrderResponse.builder()
                .orderId(entity.getId())
                .userId(entity.getUserId())
                .isSuccess(true)
                .status(PAYMENT_SUCCESS)
                .totalPrice(response.getTotalAmount())
                .message(response.getReason())
                .build();
    }

    private OrderResponse createOrderFailedResponse(OrderEntity entity, PaymentInfoResponse response) {
        return OrderResponse.builder()
                .orderId(entity.getId())
                .userId(entity.getUserId())
                .isSuccess(false)
                .status(PAYMENT_FAILED)
                .totalPrice(entity.getTotalAmount())
                .message(entity.getReason())
                .paymentIssues(response.getIssues())
                .build();
    }
}
