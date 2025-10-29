package az.ingress.dao.repository;

import az.ingress.dao.entity.PaymentInfoEntity;
import org.springframework.data.repository.CrudRepository;

public interface PaymentInfoRepository extends CrudRepository<PaymentInfoEntity, Long> {
}
