package az.ingress.dao.repository;

import az.ingress.dao.entity.ProductVariantEntity;
import org.springframework.data.repository.CrudRepository;

public interface ProductVariantRepository extends CrudRepository<ProductVariantEntity, Long> {
}
