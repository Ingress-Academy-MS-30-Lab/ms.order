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
import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductReportMapper {

    @Mapping(target = "orderNumber", source = "orderNumber")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "orderItems", source = "items")
    @Mapping(target = "totalAmount", source = "totalAmount")
    OrderReportResponse toReport(OrderEntity entity);

    @Mapping(target = "productName", source = "title")
    @Mapping(target = "imageUrl", expression = "java(resolveImageUrl(item))")
    @Mapping(target = "productPrice", expression = "java(resolvePrice(item))")
    @Mapping(target = "salePrice", expression = "java(resolveSalePrice(item))")
    @Mapping(target = "requestedQuantity", expression = "java(resolveRequestedQuantity(item))")
    OrderItemReportResponse toItemReport(OrderItemEntity item);

    List<OrderReportResponse> toReportList(List<OrderEntity> entities);

    default String resolveImageUrl(OrderItemEntity item) {
        if (item.getVariants() == null || item.getVariants().isEmpty()) return null;
        return item.getVariants().get(0).getImageUrl();
    }

    default BigDecimal resolvePrice(OrderItemEntity item) {
        if (item.getVariants() == null || item.getVariants().isEmpty()) return null;
        ProductVariantEntity variant = item.getVariants().get(0);
        return variant.isOnSale() && variant.getSalePrice() != null
                ? variant.getSalePrice()
                : variant.getPrice();
    }

    default BigDecimal resolveSalePrice(OrderItemEntity item) {
        if (item.getVariants() == null || item.getVariants().isEmpty()) return null;
        return item.getVariants().get(0).getSalePrice();
    }

    default int resolveRequestedQuantity(OrderItemEntity item) {
        if (item.getVariants() == null || item.getVariants().isEmpty()) return item.getQuantity();
        return item.getVariants().get(0).getRequestedQuantity();
    }
}
