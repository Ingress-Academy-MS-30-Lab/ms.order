package az.ingress.dao.repository;

import az.ingress.dao.entity.OrderEntity;
import az.ingress.dao.entity.PaymentInfoEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface PaymentInfoRepository extends CrudRepository<PaymentInfoEntity, Long> {

    @Query("SELECT p FROM PaymentInfoEntity p WHERE p.order=:entity ")
    PaymentInfoEntity findPaymentByOrder(OrderEntity entity);

}
