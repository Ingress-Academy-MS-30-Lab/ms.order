package az.ingress.util;

import java.util.UUID;

public class IDGenerator {

    public static String generateOrderNumber() {
        String uniquePart = UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        String datePart = java.time.LocalDate.now().toString().replace("-", "");
        return "ING-" + datePart + "-" + uniquePart;
    }
}
