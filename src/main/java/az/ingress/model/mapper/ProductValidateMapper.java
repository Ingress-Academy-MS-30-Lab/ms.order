package az.ingress.model.mapper;

import az.ingress.model.client.product.ProductItemValidateRequestDto;
import az.ingress.model.client.product.ProductValidateRequestDto;
import az.ingress.model.dto.request.OrderCreateRequest;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductValidateMapper {

    ProductValidateRequestDto toProductValidateRequest(OrderCreateRequest request);

    @AfterMapping
    default void mapItems(OrderCreateRequest request, @MappingTarget ProductValidateRequestDto target) {
        if (request.getItems() != null) {
            var flatItems = request.getItems().stream()
                    .flatMap(orderItem -> orderItem.getProductVariants().stream()
                            .map(variant -> new ProductItemValidateRequestDto(
                                    orderItem.getProductId(),
                                    variant.getProductVariantId(),
                                    variant.getRequestedQuantity()
                            ))
                    )
                    .toList();

            target.setProducts(flatItems);
        }
    }
}
