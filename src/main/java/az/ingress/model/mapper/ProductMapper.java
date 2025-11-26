package az.ingress.model.mapper;

import az.ingress.dao.entity.OrderEntity;
import az.ingress.dao.entity.OrderItemEntity;
import az.ingress.dao.entity.ProductVariantEntity;
import az.ingress.model.client.product.ProductItemReservationRequestDto;
import az.ingress.model.client.product.ProductReservationRequestDto;
import az.ingress.model.dto.response.ProductValidateItemResponse;
import az.ingress.model.dto.response.ProductValidateResponse;
import az.ingress.model.dto.response.ProductVariantsResponse;
import org.mapstruct.AfterMapping;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE, builder = @Builder(disableBuilder = true))
public interface ProductMapper {


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "orderNumber", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "items", source = "products")
    @Mapping(target = "totalAmount", source = "totalPrice")
    OrderEntity toOrderEntity(ProductValidateResponse response);

    @Mapping(target = "variants", source = "productVariants")
    OrderItemEntity toOrderItemEntity(ProductValidateItemResponse response);

    ProductVariantEntity toProductVariant(ProductVariantsResponse response);

    @Mapping(target = "orderId", source = "id")
    @Mapping(target = "userId", source = "userId")
    @Mapping(target = "products", source = "items")
    ProductReservationRequestDto toProductReservationRequest(OrderEntity orderEntity);


    @Mapping(target = "productVariants", expression = "java(mapVariantIds(item))")
    @Mapping(target = "quantity", source = "quantity")
    ProductItemReservationRequestDto toProductItemReservationRequest(OrderItemEntity item);

    @AfterMapping
    default void linkOrderRelations(@MappingTarget OrderEntity order) {
        if (order.getItems() == null) return;
        order.getItems().forEach(item -> {
            item.setOrder(order);
        });
    }

    @AfterMapping
    default void linkItemRelations(@MappingTarget OrderItemEntity item) {
        if (item.getVariants() == null) return;
        item.getVariants().forEach(v -> v.setOrderItem(item));
    }

    default List<Long> mapVariantIds(OrderItemEntity item) {
        if (item.getVariants() == null) return List.of();
        return item.getVariants().stream()
                .map(ProductVariantEntity::getProductVariantId)
                .toList();
    }
}
