package az.ingress.service.abstraction;

import az.ingress.dao.entity.OrderEntity;
import az.ingress.model.enums.OrderStatus;
import az.ingress.model.dto.request.OrderConfirmRequest;
import az.ingress.model.dto.request.OrderCreateRequest;
import az.ingress.model.dto.request.ShippingAddressRequest;
import az.ingress.model.dto.response.OrderResponse;

public interface OrderService {
    OrderResponse validateProduct(OrderCreateRequest request);

    void addingShippingInfo(Long orderId, ShippingAddressRequest request);

    OrderResponse confirmOrder(OrderConfirmRequest request);

    void updateOrderStatus(OrderEntity order, OrderStatus status);

    OrderEntity fetchOrderIfExists(Long orderId);

}
