package az.ingress.model.mapper;

import az.ingress.dao.entity.ShippingAddressEntity;
import az.ingress.model.dto.request.ShippingAddressRequest;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE, builder = @Builder(disableBuilder = true))
public interface ShippingAddressMapper {

    ShippingAddressEntity toEntity(ShippingAddressRequest request);
}
