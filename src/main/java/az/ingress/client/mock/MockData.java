package az.ingress.client.mock;

import az.ingress.model.client.payment.PaymentRequestDto;
import az.ingress.model.client.product.ProductItemReservationRequestDto;
import az.ingress.model.client.product.ProductItemValidateRequestDto;
import az.ingress.model.client.product.ProductReservationRequestDto;
import az.ingress.model.client.product.ProductValidateRequestDto;
import az.ingress.model.dto.response.PaymentInfoResponse;
import az.ingress.model.dto.response.PaymentIssueResponse;
import az.ingress.model.dto.response.ProductIssueResponse;
import az.ingress.model.dto.response.ProductReservationResponse;
import az.ingress.model.dto.response.ProductValidateItemResponse;
import az.ingress.model.dto.response.ProductValidateResponse;
import az.ingress.model.dto.response.ProductVariantsResponse;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import static az.ingress.model.enums.OrderStatus.PRODUCT_RESERVATION_COMPLETED;
import static az.ingress.model.enums.PaymentErrorType.AMOUNT_EXCEEDS_LIMIT;
import static az.ingress.model.enums.PaymentErrorType.INVALID_METHOD;
import static az.ingress.model.enums.ProductIssueType.OUT_OF_STOCK;

@Profile("dev")
public class MockData {

    private static final AtomicLong RESERVATION_ID = new AtomicLong(1000);
    private static final AtomicLong PAYMENT_ID = new AtomicLong(40);

    static public ProductValidateResponse productVariantsResponse(ProductValidateRequestDto request) {
        List<ProductValidateItemResponse> productResponses = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (ProductItemValidateRequestDto item : request.getProducts()) {

            ProductVariantsResponse variant1 = new ProductVariantsResponse(
                    item.getProductVariantId(),
                    "https://aws.com/product-" + item.getProductVariantId() + ".jpg",
                    new BigDecimal("60.00"),
                    false,
                    null,
                    item.getQuantity()
            );

            ProductVariantsResponse variant2 = new ProductVariantsResponse(
                    item.getProductVariantId(),
                    "https://aws.com/product-" + item.getProductVariantId() + ".jpg",
                    new BigDecimal("45.00"),
                    false,
                    null,
                    item.getQuantity()
            );

            ProductValidateItemResponse productItem = new ProductValidateItemResponse(
                    "Category-" + item.getProductId(),
                    item.getProductId(),
                    "Iphone 17 Black" ,
                    List.of(variant1,variant2),
                    item.getQuantity()
            );

            productResponses.add(productItem);

            total = BigDecimal.valueOf(105);
        }

        ProductValidateResponse response = new ProductValidateResponse();
        response.setProducts(productResponses);
        response.setTotalPrice(total);

        return response;
    }

    public static ProductReservationResponse productReservationResponse(ProductReservationRequestDto request) {
        ProductReservationResponse response = new ProductReservationResponse();

        response.setReservationId(RESERVATION_ID.incrementAndGet());

        response.setStatus(PRODUCT_RESERVATION_COMPLETED);
        response.setReservationExpiresAt(LocalDateTime.now().plusMinutes(5));

        List<ProductIssueResponse> issues = new ArrayList<>();

        for (ProductItemReservationRequestDto item : request.getProducts()) {

            for (Long variantId : item.getProductVariants()) {
                if (item.getProductId() % 2 == 0) {
                    ProductIssueResponse issue = new ProductIssueResponse(
                            OUT_OF_STOCK,
                            "Product is out of stock",
                            item.getProductId(),
                            variantId,
                            "Mock Product " + item.getProductId(),
                            item.getQuantity(),
                            0,
                            BigDecimal.valueOf(10),
                            BigDecimal.valueOf(12)
                    );
                    issues.add(issue);
                }
            }
        }

        if (issues==null) {
            response.setReason("All products were successfully reserved");
        } else {
            response.setReason("Some products could not be reserved");
        }


        response.setIssues(issues);

        return response;
    }

    public static PaymentInfoResponse processPayment(@RequestBody PaymentRequestDto request) {

        PaymentInfoResponse response = new PaymentInfoResponse();
        response.setOrderId(request.getOrderId());
        response.setUserId(request.getUserId());
        response.setTotalAmount(request.getAmount());
        response.setPaymentId(PAYMENT_ID.incrementAndGet());

//        List<PaymentIssueResponse> issues = new ArrayList<>();
//
//        if (request.getAmount().compareTo(new BigDecimal("50.00")) > 0) {
//            response.setSuccess(false);
//            response.setReason("Payment amount exceeds the allowed limit");
//
//            issues.add(new PaymentIssueResponse(
//                    AMOUNT_EXCEEDS_LIMIT,
//                    "Maximum allowed amount is 50 AZN"
//            ));
//
//            response.setIssues(issues);
//            return response;
//        }

        if (!List.of("CARD", "APPLE_PAY", "CASH").contains(request.getPaymentMethod())) {
            response.setSuccess(false);
            response.setReason("Invalid payment method");

//            issues.add(new PaymentIssueResponse(
//                    INVALID_METHOD,
//                    "Payment method not supported: " + request.getPaymentMethod()
//            ));
//
//            response.setIssues(issues);
            return response;
        }

        response.setSuccess(true);
        response.setReason("Payment completed successfully");
        response.setIssues(Collections.emptyList());

        return response;
    }
}

