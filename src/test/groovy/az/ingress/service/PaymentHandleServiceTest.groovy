package az.ingress.service

import az.ingress.MockTestData
import az.ingress.client.PaymentClient
import az.ingress.client.ProductClient
import az.ingress.dao.entity.OrderEntity
import az.ingress.dao.entity.PaymentInfoEntity
import az.ingress.dao.repository.OrderItemRepository
import az.ingress.dao.repository.OrderRepository
import az.ingress.dao.repository.PaymentInfoRepository
import az.ingress.model.client.payment.PaymentRequestDto
import az.ingress.model.dto.request.OrderConfirmRequest
import az.ingress.model.enums.OrderStatus
import az.ingress.model.mapper.PaymentMapper
import io.github.benas.randombeans.EnhancedRandomBuilder
import io.github.benas.randombeans.api.EnhancedRandom
import spock.lang.Specification

import static az.ingress.model.enums.OrderStatus.PAYMENT_FAILED

class PaymentHandleServiceTest extends Specification {
    def paymentClient = Mock(PaymentClient)
    def productClient = Mock(ProductClient)
    def orderRepository = Mock(OrderRepository)
    def outboxEventService = Mock(OutboxEventService)
    def orderItemRepository = Mock(OrderItemRepository)
    def paymentMapper = Mock(PaymentMapper)
    def paymentInfoRepository = Mock(PaymentInfoRepository)


    def service = new PaymentHandleServiceImpl(paymentClient, productClient,
            orderRepository, outboxEventService, orderItemRepository,
            paymentMapper, paymentInfoRepository)
    EnhancedRandom random = EnhancedRandomBuilder.aNewEnhancedRandom()


    def "should complete payment successfully"() {
        given:
        def orderEntity = random.nextObject(OrderEntity)
        def paymentInfoEntity = random.nextObject(PaymentInfoEntity)
        def paymentInfoResponse = MockTestData.paymentInfoResponse()
        def paymentRequestDto = random.nextObject(PaymentRequestDto)
        paymentInfoResponse.setSuccess(true)
        def categories = ["Electronics", "Books"]
        when:
        paymentInfoRepository.findPaymentByOrder(orderEntity) >> paymentInfoEntity
        paymentMapper.toRequest(paymentInfoEntity) >> paymentRequestDto
        orderItemRepository.findCategoriesByOrder(orderEntity) >> categories
        paymentClient.createPayment(paymentRequestDto) >> paymentInfoResponse
        def result = service.handlePaymentRequest(orderEntity)

        then:
        1 * paymentInfoRepository.save(paymentInfoEntity)
        1 * orderRepository.save(orderEntity)
        1 * outboxEventService.saveToOutbox(orderEntity, categories)
        1 * productClient.productStockCallback(_)
        result.status == OrderStatus.PAYMENT_SUCCESS
        result.success
    }

    def "should handle payment failure correctly"() {
        given:
        def orderEntity = random.nextObject(OrderEntity)
        def paymentInfoEntity = random.nextObject(PaymentInfoEntity)
        def paymentInfoResponse = MockTestData.paymentInfoResponse()
        def paymentRequestDto = random.nextObject(PaymentRequestDto)
        paymentInfoResponse.setSuccess(false)
        paymentInfoResponse.setReason("Insufficient funds")
        def categories = ["Electronics", "Books"]

        when:
        paymentInfoRepository.findPaymentByOrder(orderEntity) >> paymentInfoEntity
        paymentMapper.toRequest(paymentInfoEntity) >> paymentRequestDto
        paymentClient.createPayment(paymentRequestDto) >> paymentInfoResponse
        orderItemRepository.findCategoriesByOrder(orderEntity) >> categories
        def result = service.handlePaymentRequest(orderEntity)

        then:
        1 * paymentInfoRepository.save(paymentInfoEntity)
        1 * orderRepository.save(orderEntity)
        1 * outboxEventService.saveToOutbox(orderEntity, categories)
        result.status == PAYMENT_FAILED
        !result.success
        result.message == "Insufficient funds"
    }

    def "should update payment entity from request"() {
        given:
        def paymentInfoEntity = random.nextObject(PaymentInfoEntity)
        def orderConfirmRequest = random.nextObject(OrderConfirmRequest)
        when:
        service.updateAndPayment(paymentInfoEntity, orderConfirmRequest)
        then:
        1 * paymentMapper.updateEntity(paymentInfoEntity, orderConfirmRequest.getPayment())
        1 * paymentInfoRepository.save(paymentInfoEntity)
        paymentInfoEntity.userId == orderConfirmRequest.userId
    }
}
