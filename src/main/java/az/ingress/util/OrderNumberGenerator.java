package az.ingress.util;

import java.security.SecureRandom;
import java.time.Year;

public class OrderNumberGenerator {

    private static final String ALPHANUM = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final SecureRandom RANDOM = new SecureRandom();

    public String generateCredentialId() {
        String yearPart = String.format("%02d", Year.now().getValue() % 100);
        return "ING-" + yearPart + "-" + randomAlphanumeric();
    }

    private String randomAlphanumeric() {
        StringBuilder sb = new StringBuilder(5);
        for (int i = 0; i < 5; i++) sb.append(ALPHANUM.charAt(RANDOM.nextInt(ALPHANUM.length())));
        return sb.toString();
    }
}
