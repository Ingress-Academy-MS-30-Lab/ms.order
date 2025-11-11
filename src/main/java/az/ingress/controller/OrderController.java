package az.ingress.controller;


import az.ingress.model.dto.request.OrderConfirmRequest;
import az.ingress.model.dto.request.OrderCreateRequest;
import az.ingress.model.dto.request.OrderReportRequest;
import az.ingress.model.dto.request.ShippingAddressRequest;
import az.ingress.model.dto.response.OrderReportResponse;
import az.ingress.model.dto.response.OrderResponse;
import az.ingress.service.OrderReportService;
import az.ingress.service.abstraction.OrderService;
import az.ingress.util.ExcelGenerator;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final OrderReportService orderReportService;

    @PostMapping("/validate")
    @ResponseStatus(CREATED)
    public OrderResponse validateProduct(@RequestBody OrderCreateRequest request) {
        return orderService.validateProduct(request);
    }

    @PostMapping(path = "/{orderId}/shipping-address")
    @ResponseStatus(CREATED)
    public void addingShippingInfo(@PathVariable Long orderId, @RequestBody ShippingAddressRequest request) {
        orderService.addingShippingInfo(orderId, request);
    }

    @PostMapping("/confirm")
    @ResponseStatus(CREATED)
    public OrderResponse confirmOrder(@RequestBody OrderConfirmRequest request) {
        return orderService.confirmOrder(request);
    }

    @SneakyThrows
    @PostMapping("/report")
    public ResponseEntity<byte[]> exportToExcel(@RequestBody OrderReportRequest request) {
        List<OrderReportResponse> orderReportResponses = orderReportService.buyerOrdersByPeriod(request);
        ByteArrayOutputStream excelOutputStream = ExcelGenerator.generateExcel(orderReportResponses);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=order_report_" + LocalDate.now() + ".xlsx")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(excelOutputStream.toByteArray());
    }
}
