package az.ingress.dao.repository;

import az.ingress.dao.entity.OrderStatusEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface OrderStatusEventRepository extends CrudRepository<OrderStatusEventEntity, Long> {
}
