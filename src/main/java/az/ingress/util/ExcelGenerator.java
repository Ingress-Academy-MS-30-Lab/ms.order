package az.ingress.util;

import az.ingress.model.dto.response.OrderItemReportResponse;
import az.ingress.model.dto.response.OrderReportResponse;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class ExcelGenerator {

    public static ByteArrayOutputStream generateExcel(List<OrderReportResponse> orders) throws IOException {
        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Order Report");

            String[] columns = {
                    "Order Number",
                    "Created At",
                    "Status",
                    "Product Name",
                    "Image URL",
                    "Product Price",
                    "Sale Price",
                    "Requested Quantity",
                    "Total Amount"
            };

            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);

            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
                cell.setCellStyle(headerStyle);
            }

            int rowIndex = 1;

            for (OrderReportResponse order : orders) {
                if (order.getOrderItems() != null && !order.getOrderItems().isEmpty()) {
                    for (OrderItemReportResponse item : order.getOrderItems()) {
                        Row row = sheet.createRow(rowIndex++);
                        row.createCell(0).setCellValue(order.getOrderNumber() != null ? order.getOrderNumber() : "");
                        row.createCell(1).setCellValue(order.getCreatedAt() != null ? order.getCreatedAt().toString() : "");
                        row.createCell(2).setCellValue(order.getStatus() != null ? order.getStatus().name() : "");
                        row.createCell(3).setCellValue(Objects.toString(item.getProductName(), ""));
                        row.createCell(4).setCellValue(Objects.toString(item.getImageUrl(), ""));
                        row.createCell(5).setCellValue(item.getProductPrice() != null ? item.getProductPrice().doubleValue() : 0.0);
                        row.createCell(6).setCellValue(item.getSalePrice() != null ? item.getSalePrice().doubleValue() : 0.0);
                        row.createCell(7).setCellValue(item.getRequestedQuantity());
                        row.createCell(8).setCellValue(order.getTotalAmount() != null ? order.getTotalAmount().doubleValue() : 0.0);
                    }
                } else {
                    Row row = sheet.createRow(rowIndex++);
                    row.createCell(0).setCellValue(order.getOrderNumber() != null ? order.getOrderNumber() : "");
                    row.createCell(1).setCellValue(order.getCreatedAt() != null ? order.getCreatedAt().toString() : "");
                    row.createCell(2).setCellValue(order.getStatus() != null ? order.getStatus().name() : "");
                    row.createCell(3).setCellValue("-");
                    row.createCell(4).setCellValue("-");
                    row.createCell(5).setCellValue(0.0);
                    row.createCell(6).setCellValue(0.0);
                    row.createCell(7).setCellValue(0);
                    row.createCell(8).setCellValue(order.getTotalAmount() != null ? order.getTotalAmount().doubleValue() : 0.0);
                }
            }
            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            return out;
        }
    }

}
