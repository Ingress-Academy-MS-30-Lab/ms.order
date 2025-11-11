package az.ingress.service;

import az.ingress.client.ProductClient;
import az.ingress.dao.entity.OrderEntity;
import az.ingress.dao.entity.PaymentInfoEntity;
import az.ingress.dao.repository.OrderRepository;
import az.ingress.dao.repository.PaymentInfoRepository;
import az.ingress.dao.repository.ShippingAddressRepository;
import az.ingress.exception.AmountMismatchException;
import az.ingress.exception.NotFoundException;
import az.ingress.model.dto.request.OrderConfirmRequest;
import az.ingress.model.dto.request.OrderCreateRequest;
import az.ingress.model.dto.request.ShippingAddressRequest;
import az.ingress.model.dto.response.OrderResponse;
import az.ingress.model.dto.response.ProductValidateItemResponse;
import az.ingress.model.dto.response.ProductValidateResponse;
import az.ingress.model.enums.OrderStatus;
import az.ingress.model.mapper.ProductMapper;
import az.ingress.model.mapper.ProductValidateMapper;
import az.ingress.model.mapper.ShippingAddressMapper;
import az.ingress.service.abstraction.OrderService;
import az.ingress.service.abstraction.PaymentHandleService;
import az.ingress.service.abstraction.ProductReservationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static az.ingress.exception.ExceptionConstants.AMOUNT_MISMATCH;
import static az.ingress.exception.ExceptionConstants.ORDER_NOT_FOUND;
import static az.ingress.model.enums.OrderStatus.PENDING;
import static az.ingress.model.enums.OrderStatus.PRODUCT_CHECK_PENDING;
import static az.ingress.util.IDGenerator.generateOrderNumber;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductValidateMapper productValidateMapper;
    private final ProductClient productClient;
    private final ProductMapper productMapper;
    private final ProductReservationService productReservationService;
    private final ShippingAddressMapper shippingAddressMapper;
    private final ShippingAddressRepository shippingAddressRepository;
    private final OutboxEventService outboxEventService;
    private final PaymentInfoRepository paymentInfoRepository;
    private final PaymentHandleService paymentHandleService;

    @Transactional
    public OrderResponse validateProduct(OrderCreateRequest request) {
        var validateRequest = productValidateMapper.toProductValidateRequest(request);
        var productValidateResponse = productClient.validateProduct(validateRequest);
        var orderEntity = productMapper.toOrderEntity(productValidateResponse);

        orderEntity.setOrderNumber(generateOrderNumber());
        orderEntity.setUserId(request.getUserId());
        orderEntity.setStatus(PENDING);
        createPaymentEntity(orderEntity, productValidateResponse.getTotalPrice());
        var savedOrder = orderRepository.save(orderEntity);
        List<String> categories = findCategories(productValidateResponse);
        outboxEventService.saveToOutbox(savedOrder, categories);

        return OrderResponse.builder()
                .orderId(savedOrder.getId())
                .userId(savedOrder.getUserId())
                .status(PENDING)
                .totalPrice(savedOrder.getTotalAmount())
                .build();
    }

    public void addingShippingInfo(Long orderId, ShippingAddressRequest request) {
        var orderEntity = fetchOrderIfExists(orderId);
        var shippingAddress = shippingAddressMapper.toEntity(request);
        shippingAddressRepository.save(shippingAddress);
        orderEntity.setShippingAddress(shippingAddress);
        orderRepository.save(orderEntity);
    }

    @Transactional
    public OrderResponse confirmOrder(OrderConfirmRequest request) {
        var orderEntity = fetchOrderIfExists(request.getOrderId());
        var payment = paymentInfoRepository.findPaymentByOrder(orderEntity);
        if (payment.getAmount().compareTo(request.getPayment().getAmount()) != 0) {
            throw new AmountMismatchException(
                    AMOUNT_MISMATCH.getCode(),
                    AMOUNT_MISMATCH.format(payment.getAmount(), request.getPayment().getAmount())
            );
        }

        paymentHandleService.updateAndPayment(payment, request);
        updateOrderStatus(orderEntity, PRODUCT_CHECK_PENDING);
        return productReservationService.handleProductReserve(orderEntity);
    }

    public List<String> findCategories(ProductValidateResponse response) {
        return response.getProducts()
                .stream()
                .map(ProductValidateItemResponse::getCategory).toList();
    }

    public void createPaymentEntity(OrderEntity entity, BigDecimal amount) {
        PaymentInfoEntity paymentInfoEntity = PaymentInfoEntity.builder()
                .amount(amount)
                .paymentSuccess(false)
                .order(entity)
                .build();
        entity.setPaymentInfo(paymentInfoEntity);
    }

    public void updateOrderStatus(OrderEntity order, OrderStatus status) {
        order.setStatus(status);
        orderRepository.save(order);
    }

    public OrderEntity fetchOrderIfExists(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(() ->
                new NotFoundException(
                        ORDER_NOT_FOUND.getCode(),
                        ORDER_NOT_FOUND.format(orderId))
        );
    }
}
