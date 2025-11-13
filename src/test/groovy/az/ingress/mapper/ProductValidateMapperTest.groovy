package az.ingress.mapper


import az.ingress.model.dto.request.OrderCreateRequest
import az.ingress.model.dto.request.OrderItemCreateRequest
import az.ingress.model.mapper.ProductValidateMapper
import io.github.benas.randombeans.EnhancedRandomBuilder
import io.github.benas.randombeans.api.EnhancedRandom
import org.mapstruct.factory.Mappers
import spock.lang.Specification

class ProductValidateMapperTest extends Specification {

    ProductValidateMapper mapper = Mappers.getMapper(ProductValidateMapper)
    EnhancedRandom random = EnhancedRandomBuilder.aNewEnhancedRandom()

    def "should map OrderCreateRequest to ProductValidateRequestDto successfully"() {
        given:
        def orderCreateRequest = random.nextObject(OrderCreateRequest)
        when:
        def result = mapper.toProductValidateRequest(orderCreateRequest)

        then:
        result != null
        result.products[0].productId == orderCreateRequest.items[0].productId
        result.products[0].productVariantId == orderCreateRequest.items[0].productVariants[0].productVariantId
    }

    def "should handle empty items list gracefully (failed case)"() {
        given:
        def orderRequest = new OrderCreateRequest(items: [])

        when:
        def result = mapper.toProductValidateRequest(orderRequest)

        then:
        result != null
        result.products.isEmpty()
    }

    def "should handle null items list gracefully (failed case)"() {
        given:
        def orderRequest = new OrderCreateRequest(items: null)

        when:
        def result = mapper.toProductValidateRequest(orderRequest)

        then:
        result != null
    }

    def "should handle null productVariants in item gracefully (failed case)"() {
        given:
        def item = new OrderItemCreateRequest(productId: 10L, productVariants: null)
        def orderRequest = new OrderCreateRequest(items: [item])

        when:
        def result = mapper.toProductValidateRequest(orderRequest)

        then:
        thrown(NullPointerException)
        result == null
    }
}
