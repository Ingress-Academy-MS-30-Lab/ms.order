package az.ingress.mapper

import az.ingress.dao.entity.OrderEntity
import az.ingress.dao.entity.OrderItemEntity
import az.ingress.dao.entity.ProductVariantEntity
import az.ingress.model.mapper.ProductReportMapper
import io.github.benas.randombeans.EnhancedRandomBuilder
import io.github.benas.randombeans.api.EnhancedRandom
import org.mapstruct.factory.Mappers
import spock.lang.Specification

class ProductReportMapperTest extends Specification {

    ProductReportMapper mapper = Mappers.getMapper(ProductReportMapper)

    EnhancedRandom random = EnhancedRandomBuilder.aNewEnhancedRandom()

    def "should map OrderEntity to OrderReportResponse successfully"() {
        given:
        def orderEntity = random.nextObject(OrderEntity)
        when:
        def orderReportResponse = mapper.toReport(orderEntity)
        then:
        orderEntity.totalAmount == orderReportResponse.totalAmount
        orderEntity.status == orderReportResponse.status
    }

    def "should map OrderItemEntity to OrderItemReportResponse successfully"() {
        given:
        def orderItemEntity = random.nextObject(OrderItemEntity)
        when:
        def orderItemReportResponse = mapper.toItemReport(orderItemEntity)
        then:
        orderItemReportResponse.productName == orderItemEntity.title
        orderItemReportResponse.imageUrl == orderItemEntity.variants[0].getImageUrl()
        orderItemReportResponse.productPrice == orderItemEntity.variants[0].price
        orderItemReportResponse.salePrice == orderItemEntity.variants[0].salePrice
        orderItemReportResponse.requestedQuantity == orderItemEntity.variants[0].requestedQuantity
    }


    def "should resolve imageUrl successfully when variants exist"() {
        given:
        def orderItemEntity = random.nextObject(OrderItemEntity)
        when:
        def imageUrl = mapper.resolveImageUrl(orderItemEntity)
        then:
        orderItemEntity.variants[0].getImageUrl() == imageUrl
    }

    def "should resolve correct price when variant is on sale"() {
        given:
        def orderItemEntity = random.nextObject(OrderItemEntity)
        when:
        def price = mapper.resolvePrice(orderItemEntity)
        then:
        orderItemEntity.variants[0].getPrice() == price
    }

    def "should resolve sale price successfully when salePrice is present"() {
        given:
        def orderItemEntity = random.nextObject(OrderItemEntity)
        when:
        def salePrice = mapper.resolveSalePrice(orderItemEntity)
        then:
        orderItemEntity.variants[0].getSalePrice() == salePrice
    }

    def "should resolve requested quantity successfully when variants exist"() {
        given:
        def orderItemEntity = random.nextObject(OrderItemEntity)
        when:
        def requestedQuantity = mapper.resolveRequestedQuantity(orderItemEntity)
        then:
        orderItemEntity.variants[0].getRequestedQuantity() == requestedQuantity
    }

    def "toItemReport returns nulls when variants list is null"() {
        given:
        def orderItemEntity = new OrderItemEntity(
                title: "Test Product",
                quantity: 5,
                variants: null
        )

        when:
        def response = mapper.toItemReport(orderItemEntity)

        then:
        response.productName == "Test Product"
        response.imageUrl == null
        response.productPrice == null
        response.salePrice == null
    }

    def "toItemReport returns nulls when variants list is empty"() {
        given:
        def orderItemEntity = new OrderItemEntity(
                title: "Test Product",
                quantity: 5,
                variants: []
        )

        when:
        def response = mapper.toItemReport(orderItemEntity)

        then:
        response.productName == "Test Product"
        response.imageUrl == null
        response.productPrice == null
        response.salePrice == null
    }

    def "resolvePrice returns ZERO when variant price is null"() {
        given:
        def variant = new ProductVariantEntity(price: null)
        def orderItemEntity = new OrderItemEntity(
                title: "Test Product",
                variants: [variant]
        )

        when:
        def price = mapper.resolvePrice(orderItemEntity)

        then:
        price == BigDecimal.ZERO
    }

    def "resolveImageUrl returns null when item is null"() {
        when:
        def result = mapper.resolveImageUrl(null)
        then:
        result == null
    }

    def "resolveSalePrice returns null when variants are null"() {
        given:
        def orderItemEntity = new OrderItemEntity(
                title: "Test Product",
                variants: null
        )

        when:
        def salePrice = mapper.resolveSalePrice(orderItemEntity)

        then:
        salePrice == null
    }

    def "resolveRequestedQuantity returns item quantity when variants are null or empty"() {
        given:
        def orderItemEntityNull = new OrderItemEntity(quantity: 7, variants: null)
        def orderItemEntityEmpty = new OrderItemEntity(quantity: 5, variants: [])

        expect:
        mapper.resolveRequestedQuantity(orderItemEntityNull) == 0
        mapper.resolveRequestedQuantity(orderItemEntityEmpty) == 0
    }

}
