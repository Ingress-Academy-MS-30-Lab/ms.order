package az.ingress.spesification;

import az.ingress.dao.entity.OrderEntity;
import az.ingress.model.dto.request.OrderReportRequest;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.Objects;

public class OrderSpecification {

    public static Specification<OrderEntity> build(OrderReportRequest request) {
        return Specification.where(byBuyerId(request.getBuyerId()))
                .and(byDateRange(request.getStartDate(), request.getEndDate()));
    }

    private static Specification<OrderEntity> byBuyerId(Long buyerId) {
        return (root, query, cb) -> {
            if (Objects.isNull(buyerId)) {
                return cb.conjunction();
            }
            return cb.equal(root.get("userId"), buyerId);
        };
    }

    private static Specification<OrderEntity> byDateRange(
            java.time.LocalDate startDate,
            java.time.LocalDate endDate
    ) {
        return (root, query, cb) -> {
            if (Objects.isNull(startDate) && Objects.isNull(endDate)) {
                return cb.conjunction();
            }
            if (Objects.nonNull(startDate) && Objects.nonNull(endDate)) {
                LocalDateTime start = startDate.atStartOfDay();
                LocalDateTime end = endDate.atTime(23, 59, 59);
                return cb.between(root.get("createdAt"), start, end);
            }
            if (Objects.nonNull(startDate)) {
                LocalDateTime start = startDate.atStartOfDay();
                return cb.greaterThanOrEqualTo(root.get("createdAt"), start);
            } else {
                LocalDateTime end = endDate.atTime(23, 59, 59);
                return cb.lessThanOrEqualTo(root.get("createdAt"), end);
            }
        };
    }
}
