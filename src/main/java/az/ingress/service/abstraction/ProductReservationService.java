package az.ingress.service.abstraction;

import az.ingress.dao.entity.OrderEntity;
import az.ingress.model.dto.response.OrderResponse;
import az.ingress.model.dto.response.ProductReservationResponse;

public interface ProductReservationService {
    OrderResponse handleProductReserve(OrderEntity entity);

    OrderResponse handleProductReservation(ProductReservationResponse response, OrderEntity entity);
}
