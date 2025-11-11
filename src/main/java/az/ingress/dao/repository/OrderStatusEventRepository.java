package az.ingress.dao.repository;

import az.ingress.dao.entity.OrderStatusEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderStatusEventRepository extends JpaRepository<OrderStatusEventEntity, Long> {
}
