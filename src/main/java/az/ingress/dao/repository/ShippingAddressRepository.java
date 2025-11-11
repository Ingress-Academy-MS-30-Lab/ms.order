package az.ingress.dao.repository;

import az.ingress.dao.entity.ShippingAddressEntity;
import org.springframework.data.repository.CrudRepository;

public interface ShippingAddressRepository extends CrudRepository<ShippingAddressEntity, Long> {
}
