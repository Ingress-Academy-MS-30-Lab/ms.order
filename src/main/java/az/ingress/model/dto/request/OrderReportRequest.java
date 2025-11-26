package az.ingress.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderReportRequest {
    private Long buyerId;
    private LocalDate startDate;
    private LocalDate endDate;
}