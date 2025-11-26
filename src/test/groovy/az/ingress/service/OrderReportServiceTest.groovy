package az.ingress.service

import az.ingress.dao.entity.OrderEntity
import az.ingress.dao.repository.OrderRepository
import az.ingress.model.dto.request.OrderReportRequest
import az.ingress.model.dto.response.OrderReportResponse
import az.ingress.model.mapper.ProductReportMapper
import az.ingress.spesification.OrderSpecification
import io.github.benas.randombeans.EnhancedRandomBuilder
import io.github.benas.randombeans.api.EnhancedRandom
import spock.lang.Specification

class OrderReportServiceTest extends Specification {

    def orderRepository = Mock(OrderRepository)
    def productReportMapper = Mock(ProductReportMapper)
    EnhancedRandom random = EnhancedRandomBuilder.aNewEnhancedRandom()
    def service = new OrderReportService(orderRepository, productReportMapper)

    def "jh"() {
        given:
        def orderReportRequest = random.nextObject(OrderReportRequest)
        def orderEntity = random.nextObject(OrderEntity)
        def specification = OrderSpecification.build(orderReportRequest)
        when:
        def orders = orderRepository.findAll { specification }
        def orderReportResponse = productReportMapper.toReport(orderEntity)
        def result = service.buyerOrdersByPeriod(orderReportRequest)
        then:
        orders != null
        result.size() != 0
    }
}
