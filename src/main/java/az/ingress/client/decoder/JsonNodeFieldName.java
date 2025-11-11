package az.ingress.client.decoder;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum JsonNodeFieldName {
    MESSAGE("Feign error");

    private final String value;
}
