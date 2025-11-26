package az.ingress.dao.repository;

import az.ingress.dao.entity.OrderEntity;
import az.ingress.dao.entity.OrderItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OrderItemRepository extends CrudRepository<OrderItemEntity, Long> {

    @Query("SELECT oi.category FROM OrderItemEntity oi WHERE oi.order=:entity")
    List<String> findCategoriesByOrder(OrderEntity entity);
}
