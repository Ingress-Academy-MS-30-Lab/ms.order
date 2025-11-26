package az.ingress.service.abstraction;

import az.ingress.dao.entity.OrderEntity;
import az.ingress.model.dto.response.OrderResponse;

public interface ProductReservationService {
    OrderResponse handleProductReserve(OrderEntity entity);

}
