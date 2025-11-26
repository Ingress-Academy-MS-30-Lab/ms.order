package az.ingress.dao.repository;

import az.ingress.dao.entity.OrderOutboxEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OrderOutboxRepository extends CrudRepository<OrderOutboxEntity, Long> {

    @Query("SELECT o FROM OrderOutboxEntity o WHERE o.processed = false ORDER BY o.createdAt ASC")
    List<OrderOutboxEntity> findUnprocessedEvents();

    @Query("SELECT o FROM OrderOutboxEntity o WHERE o.processed = true")
    List<OrderOutboxEntity> findProcessedEvents();
}
