package az.ingress.mapper

import az.ingress.dao.entity.OrderEntity
import az.ingress.dao.entity.OrderItemEntity
import az.ingress.model.dto.response.ProductValidateItemResponse
import az.ingress.model.dto.response.ProductValidateResponse
import az.ingress.model.dto.response.ProductVariantsResponse
import az.ingress.model.mapper.ProductMapper
import io.github.benas.randombeans.EnhancedRandomBuilder
import io.github.benas.randombeans.api.EnhancedRandom
import org.mapstruct.factory.Mappers
import spock.lang.Specification

class ProductMapperTest extends Specification {

    ProductMapper mapper = Mappers.getMapper(ProductMapper)

    EnhancedRandom random = EnhancedRandomBuilder.aNewEnhancedRandom()

    def "toOrderEntity maps ProductValidateResponse to OrderEntity correctly"() {
        given:
        def productVariant = new ProductVariantsResponse(
                productVariantId: random.nextObject(Long),
                imageUrl: random.nextObject(String),
                price: random.nextObject(BigDecimal),
                onSale: true,
                salePrice: random.nextObject(BigDecimal),
                requestedQuantity: random.nextObject(Integer)
        )
        def itemResponse = new ProductValidateItemResponse(
                category: random.nextObject(String),
                productId: random.nextObject(Long),
                title: random.nextObject(String),
                productVariants: [productVariant],
                quantity: random.nextObject(Integer)
        )

        def response = new ProductValidateResponse(
                products: [itemResponse],
                totalPrice: 100.0
        )

        when:
        OrderEntity order = mapper.toOrderEntity(response)

        then:
        order != null
        order.items != null
        order.items.size() == 1
        order.items[0].order == order
        order.items[0].variants != null
        order.items[0].quantity == itemResponse.quantity
    }

    def "toOrderItemEntity maps ProductValidateItemResponse to OrderItemEntity correctly"() {
        given:
        def productValidateItemResponse = random.nextObject(ProductValidateItemResponse)
        when:
        def orderItemEntity = mapper.toOrderItemEntity(productValidateItemResponse)

        then:
        orderItemEntity != null
        orderItemEntity.category == productValidateItemResponse.category
        orderItemEntity.title == productValidateItemResponse.title
        orderItemEntity.productId == productValidateItemResponse.productId
        orderItemEntity.variants[0] != null
    }

    def "toProductVariant maps ProductVariantsResponse to ProductVariantEntity correctly"() {
        given:
        def productVariantResponse = random.nextObject(ProductVariantsResponse)
        when:
        def variant = mapper.toProductVariant(productVariantResponse)
        then:
        variant != null
        variant.productVariantId == productVariantResponse.productVariantId
        variant.requestedQuantity == productVariantResponse.requestedQuantity
        variant.orderItem == null
    }

    def "toProductReservationRequest maps OrderEntity to ProductReservationRequestDto correctly"() {
        given:
        def orderEntity = random.nextObject(OrderEntity)

        when:
        def request = mapper.toProductReservationRequest(orderEntity)

        then:
        request != null
        request.userId == orderEntity.userId
        request.orderId == orderEntity.id
        request.products != null
        request.products[0].productId == orderEntity.items[0].productId
        request.products.size() == orderEntity.items.size()
    }

    def "toProductItemReservationRequest maps OrderItemEntity to ProductItemReservationRequestDto correctly"() {
        given:
        def orderItemEntity = random.nextObject(OrderItemEntity)
        when:
        def request = mapper.toProductItemReservationRequest(orderItemEntity)
        then:
        orderItemEntity.variants.eachWithIndex { variant, i ->
            assert variant.productVariantId == request.productVariants[i]
        }
        orderItemEntity.quantity == request.quantity
        orderItemEntity.productId == request.productId
    }

    def "linkOrderRelations correctly"() {
        given:
        def orderEntity = random.nextObject(OrderEntity)
        orderEntity.items?.each { it.order = null }
        when:
        mapper.linkOrderRelations(orderEntity)
        then:
        orderEntity.items.every { it.order == orderEntity }
    }

    def "linkItemRelations correctly"() {
        given:
        def orderItemEntity = random.nextObject(OrderItemEntity)
        orderItemEntity.variants?.each { it.orderItem = null }
        when:
        mapper.linkItemRelations(orderItemEntity)
        then:
        orderItemEntity.variants != null
        orderItemEntity.variants.every { it.orderItem == orderItemEntity }
    }

    def "mapVariantIds correctly"() {
        given:
        def orderItemEntity = random.nextObject(OrderItemEntity)
        when:
        def ids = mapper.mapVariantIds(orderItemEntity)
        then:
        orderItemEntity.variants != null
        !ids.isEmpty()
        orderItemEntity.variants[0].productVariantId == ids[0]
    }

    def "toOrderEntity returns empty order when products is null"() {
        given:
        def response = new ProductValidateResponse(products: null, totalPrice: 0.0)

        when:
        def order = mapper.toOrderEntity(response)

        then:
        order != null
        order.items == null
        order.totalAmount == 0.0
    }

    def "toOrderItemEntity handles null variants gracefully"() {
        given:
        def productValidateItemResponse = random.nextObject(ProductValidateItemResponse)
        productValidateItemResponse.productVariants = null

        when:
        def orderItemEntity = mapper.toOrderItemEntity(productValidateItemResponse)

        then:
        orderItemEntity != null
        orderItemEntity.variants == null
    }

    def "toProductVariant returns null when input is null"() {
        when:
        def variant = mapper.toProductVariant(null)

        then:
        variant == null
    }

    def "toProductItemReservationRequest handles empty variants list"() {
        given:
        def orderItemEntity = random.nextObject(OrderItemEntity)
        orderItemEntity.variants = []

        when:
        def request = mapper.toProductItemReservationRequest(orderItemEntity)

        then:
        request != null
        request.productVariants.isEmpty()
    }

    def "linkOrderRelations handles null items"() {
        given:
        def orderEntity = random.nextObject(OrderEntity)
        orderEntity.items = null

        when:
        mapper.linkOrderRelations(orderEntity)

        then:
        orderEntity.items == null
    }

    def "linkItemRelations handles null variants"() {
        given:
        def orderItemEntity = random.nextObject(OrderItemEntity)
        orderItemEntity.variants = null

        when:
        mapper.linkItemRelations(orderItemEntity)

        then:
        orderItemEntity.variants == null
    }

    def "toOrderItemEntity handles negative quantity"() {
        given:
        def itemResponse = random.nextObject(ProductValidateItemResponse)
        itemResponse.quantity = -5

        when:
        def orderItemEntity = mapper.toOrderItemEntity(itemResponse)

        then:
        orderItemEntity.quantity == -5
    }

    def "mapVariantIds handles negative productVariantId"() {
        given:
        def orderItemEntity = random.nextObject(OrderItemEntity)
        orderItemEntity.variants[0].productVariantId = -12345L

        when:
        def ids = mapper.mapVariantIds(orderItemEntity)

        then:
        ids[0] == -12345L
    }

    def "toProductVariant handles salePrice higher than price"() {
        given:
        def variantResponse = random.nextObject(ProductVariantsResponse)
        variantResponse.price = BigDecimal.valueOf(10)
        variantResponse.salePrice = BigDecimal.valueOf(15)

        when:
        def variant = mapper.toProductVariant(variantResponse)

        then:
        variant.price == BigDecimal.valueOf(10)
        variant.salePrice == BigDecimal.valueOf(15)
    }

}
