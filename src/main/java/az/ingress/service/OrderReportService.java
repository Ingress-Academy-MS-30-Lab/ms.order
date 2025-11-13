package az.ingress.service;

import az.ingress.dao.entity.OrderEntity;
import az.ingress.dao.repository.OrderRepository;
import az.ingress.model.dto.request.OrderReportRequest;
import az.ingress.model.dto.response.OrderReportResponse;
import az.ingress.model.mapper.ProductReportMapper;
import az.ingress.spesification.OrderSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderReportService {

    private final OrderRepository orderRepository;
    private final ProductReportMapper productReportMapper;

    @Transactional
    public List<OrderReportResponse> buyerOrdersByPeriod(OrderReportRequest request) {
        List<OrderEntity> orders = orderRepository.findAll(
                OrderSpecification.build(request)
        );
        return orders.stream().map(productReportMapper::toReport).toList();
    }

}