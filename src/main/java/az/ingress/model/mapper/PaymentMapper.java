package az.ingress.model.mapper;

import az.ingress.dao.entity.PaymentInfoEntity;
import az.ingress.model.client.payment.PaymentRequestDto;
import az.ingress.model.dto.request.PaymentInfoRequest;
import az.ingress.model.dto.response.PaymentInfoResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PaymentMapper {

    PaymentRequestDto toRequest(PaymentInfoEntity entity);

    @Mapping(target = "amount", source = "totalAmount")
    @Mapping(target = "userId", source = "userId")
    @Mapping(target = "reason", source = "reason")
    @Mapping(target = "paymentSuccess", source = "success")
    void updateEntity(@MappingTarget PaymentInfoEntity entity, PaymentInfoResponse response);

    void updateEntity(@MappingTarget PaymentInfoEntity entity, PaymentInfoRequest request);
}
