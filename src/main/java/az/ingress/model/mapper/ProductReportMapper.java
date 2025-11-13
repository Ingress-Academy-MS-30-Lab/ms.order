package az.ingress.model.mapper;

import az.ingress.dao.entity.OrderEntity;
import az.ingress.dao.entity.OrderItemEntity;
import az.ingress.dao.entity.ProductVariantEntity;
import az.ingress.model.dto.response.OrderItemReportResponse;
import az.ingress.model.dto.response.OrderReportResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.math.BigDecimal;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductReportMapper {


    @Mapping(target = "orderItems", source = "items")
    OrderReportResponse toReport(OrderEntity entity);

    @Mapping(target = "productName", source = "title")
    @Mapping(target = "imageUrl", expression = "java(resolveImageUrl(item))")
    @Mapping(target = "productPrice", expression = "java(resolvePrice(item))")
    @Mapping(target = "salePrice", expression = "java(resolveSalePrice(item))")
    @Mapping(target = "requestedQuantity", expression = "java(resolveRequestedQuantity(item))")
    OrderItemReportResponse toItemReport(OrderItemEntity item);

    default String resolveImageUrl(OrderItemEntity item) {
        if (item == null || item.getVariants() == null || item.getVariants().isEmpty()) return null;
        return item.getVariants().get(0).getImageUrl();
    }

    default BigDecimal resolvePrice(OrderItemEntity item) {
        if (item == null || item.getVariants() == null || item.getVariants().isEmpty()) return null;
        ProductVariantEntity variant = item.getVariants().get(0);
        return variant.getPrice() != null ? variant.getPrice() : BigDecimal.ZERO;
    }

    default BigDecimal resolveSalePrice(OrderItemEntity item) {
        if (item == null || item.getVariants() == null || item.getVariants().isEmpty()) return null;
        return item.getVariants().get(0).getSalePrice();
    }

    default int resolveRequestedQuantity(OrderItemEntity item) {
        if (item == null || item.getVariants() == null || item.getVariants().isEmpty()) return 0;
        return item.getVariants().get(0).getRequestedQuantity();
    }
}
