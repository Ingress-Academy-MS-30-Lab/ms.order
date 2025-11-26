package az.ingress.controller

import az.ingress.MockTestData
import az.ingress.model.dto.request.OrderCreateRequest
import az.ingress.model.dto.request.ShippingAddressRequest
import az.ingress.service.OrderReportService
import az.ingress.service.abstraction.OrderService
import io.github.benas.randombeans.EnhancedRandomBuilder
import io.github.benas.randombeans.api.EnhancedRandom
import spock.lang.Specification

class OrderControllerTest extends Specification {
    def orderService = Mock(OrderService)
    def orderReportService = Mock(OrderReportService)
    def orderController = new OrderController(orderService, orderReportService)
    EnhancedRandom random = EnhancedRandomBuilder.aNewEnhancedRandom()

    def "validateProduct should return OrderResponse when request is valid"() {
        given:
        def request = random.nextObject(OrderCreateRequest)
        def expectedResponse = MockTestData.orderResponse()

        when:
        orderService.validateProduct(request) >> expectedResponse
        def response = orderController.validateProduct(request)
        then:
        response == expectedResponse
        1 * orderService.validateProduct(request)
    }

    def "addingShippingInfo should call orderService.addingShippingInfo with correct parameters"() {
        given:
        def orderId = random.nextInt()
        def request = random.nextObject(ShippingAddressRequest)

        when:
        orderController.addingShippingInfo(orderId, request)

        then:
        1 * orderService.addingShippingInfo(orderId, request)
    }
}
