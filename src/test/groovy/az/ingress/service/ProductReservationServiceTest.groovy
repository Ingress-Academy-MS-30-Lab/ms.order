package az.ingress.service

import az.ingress.client.ProductClient
import az.ingress.dao.entity.OrderEntity
import az.ingress.dao.repository.OrderRepository
import az.ingress.exception.ReservationExpiredException
import az.ingress.model.client.product.ProductReservationRequestDto
import az.ingress.model.dto.response.OrderResponse
import az.ingress.model.dto.response.ProductIssueResponse
import az.ingress.model.dto.response.ProductReservationResponse
import az.ingress.model.enums.OrderStatus
import az.ingress.model.mapper.ProductMapper
import az.ingress.service.abstraction.PaymentHandleService
import io.github.benas.randombeans.EnhancedRandomBuilder
import io.github.benas.randombeans.api.EnhancedRandom
import spock.lang.Specification

import java.time.LocalDateTime

import static az.ingress.model.enums.OrderStatus.PRODUCT_RESERVATION_COMPLETED

class ProductReservationServiceTest extends Specification {
    def productMapper = Mock(ProductMapper)
    def productClient = Mock(ProductClient)
    def paymentHandleService = Mock(PaymentHandleService)
    def orderRepository = Mock(OrderRepository)

    def service = new ProductReservationServiceImpl(productMapper, productClient, paymentHandleService, orderRepository)
    EnhancedRandom random = EnhancedRandomBuilder.aNewEnhancedRandom()

    def "should complete reservation when reservation is still valid"() {
        given:
        def entity = random.nextObject(OrderEntity)
        def reservationRequest = random.nextObject(ProductReservationRequestDto)
        def response = random.nextObject(ProductReservationResponse)
        response.setReservationExpiresAt(LocalDateTime.now().plusMinutes(10))
        response.setReservationId(random.nextLong())
        def expectedOrderResponse = random.nextObject(OrderResponse)

        when:
        productMapper.toProductReservationRequest(entity) >> reservationRequest
        productClient.reserveProducts(reservationRequest) >> response
        paymentHandleService.handlePaymentRequest(entity) >> expectedOrderResponse

        def result = service.handleProductReserve(entity)

        then:
        1 * orderRepository.save(_)
        result == expectedOrderResponse
        entity.status == PRODUCT_RESERVATION_COMPLETED
        entity.reservationId == response.getReservationId().toString()
    }

    def "should handle failed reservation when expired but issues are null"() {
        given:
        def entity = random.nextObject(OrderEntity)
        def reservationRequest = random.nextObject(ProductReservationRequestDto)
        def response = random.nextObject(ProductReservationResponse)
        response.setReservationExpiresAt(LocalDateTime.now().minusMinutes(5))
        response.setIssues(null)
        response.setReason("Product out of stock")

        when:
        productMapper.toProductReservationRequest(entity) >> reservationRequest
        productClient.reserveProducts(reservationRequest) >> response

        def result = service.handleProductReserve(entity)

        then:
        1 * orderRepository.save(_)
        result.status == OrderStatus.PRODUCT_RESERVATION_FAILED
        result.message == "Product out of stock"
        entity.status == OrderStatus.PRODUCT_RESERVATION_FAILED
    }

    def "should throw ReservationExpiredException when expired and issues not null"() {
        given:
        def entity = random.nextObject(OrderEntity)
        def reservationRequest = random.nextObject(ProductReservationRequestDto)
        def response = random.nextObject(ProductReservationResponse)
        response.setReservationExpiresAt(LocalDateTime.now().minusMinutes(5))
        response.setIssues(List.of("Variant not available") as List<ProductIssueResponse>)

        when:
        productMapper.toProductReservationRequest(entity) >> reservationRequest
        productClient.reserveProducts(reservationRequest) >> response

        service.handleProductReserve(entity)

        then:
        thrown(ReservationExpiredException)
        0 * paymentHandleService.handlePaymentRequest(_)
    }

    def "should return proper failed response from handleProductFailedResponse"() {
        given:
        def response = random.nextObject(ProductReservationResponse)
        response.setReason("Price changed")
        def entity = random.nextObject(OrderEntity)

        when:
        def result = service.handleProductFailedResponse(response, entity)

        then:
        1 * orderRepository.save(_)
        result.status == OrderStatus.PRODUCT_RESERVATION_FAILED
        result.message == response.reason
        result.userId == entity.userId
        result.orderId == entity.id
    }
}