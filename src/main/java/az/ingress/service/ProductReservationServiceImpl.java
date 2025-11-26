package az.ingress.service;

import az.ingress.client.ProductClient;
import az.ingress.dao.entity.OrderEntity;
import az.ingress.dao.repository.OrderRepository;
import az.ingress.exception.ReservationExpiredException;
import az.ingress.model.dto.response.OrderResponse;
import az.ingress.model.dto.response.ProductReservationResponse;
import az.ingress.model.mapper.ProductMapper;
import az.ingress.service.abstraction.PaymentHandleService;
import az.ingress.service.abstraction.ProductReservationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static az.ingress.exception.ExceptionConstants.RESERVATION_EXPIRED_EXCEPTION;
import static az.ingress.model.enums.OrderStatus.PRODUCT_RESERVATION_COMPLETED;
import static az.ingress.model.enums.OrderStatus.PRODUCT_RESERVATION_FAILED;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductReservationServiceImpl implements ProductReservationService {


    private final ProductMapper productMapper;
    private final ProductClient productClient;
    private final PaymentHandleService paymentHandleService;
    private final OrderRepository orderRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public OrderResponse handleProductReserve(OrderEntity entity) {
        var productReservationRequest = productMapper.toProductReservationRequest(entity);
        var response = productClient.reserveProducts(productReservationRequest);

        if (!response.getReservationExpiresAt().isBefore(LocalDateTime.now())) {
            entity.setStatus(PRODUCT_RESERVATION_COMPLETED);
            entity.setReservationId(response.getReservationId().toString());
            entity.setReservationExpireAt(response.getReservationExpiresAt());
            orderRepository.save(entity);
            return paymentHandleService.handlePaymentRequest(entity);
        }
        if (response.getIssues() == null) {
            return handleProductFailedResponse(response, entity);
        }
        throw new ReservationExpiredException(
                RESERVATION_EXPIRED_EXCEPTION.getCode(),
                RESERVATION_EXPIRED_EXCEPTION.getMessage());
    }


    public OrderResponse handleProductFailedResponse(ProductReservationResponse response, OrderEntity order) {
        order.setStatus(PRODUCT_RESERVATION_FAILED);
        order.setErrorReason(response.getReason());
        orderRepository.save(order);
        return OrderResponse.builder()
                .orderId(order.getId())
                .userId(order.getUserId())
                .status(PRODUCT_RESERVATION_FAILED)
                .message(response.getReason())
                .productIssues(response.getIssues())
                .build();
    }


}
