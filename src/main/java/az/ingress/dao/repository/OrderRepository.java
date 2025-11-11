package az.ingress.dao.repository;

import az.ingress.dao.entity.OrderEntity;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<OrderEntity, Long>,
        JpaSpecificationExecutor<OrderEntity> {

    @EntityGraph(attributePaths = {"items"})
    @Override
    List<OrderEntity> findAll(Specification<OrderEntity> spec);

    @Override
    @EntityGraph(attributePaths = {"items", "items.variants"})
    Optional<OrderEntity> findById(Long orderId);

}
