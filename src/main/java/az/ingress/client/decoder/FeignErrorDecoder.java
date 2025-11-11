package az.ingress.client.decoder;

import az.ingress.exception.CustomFeignException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static az.ingress.client.decoder.JsonNodeFieldName.MESSAGE;
import static az.ingress.exception.ExceptionConstants.CLIENT_ERROR;

@Slf4j
@Component
public class FeignErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        var errorMessage = CLIENT_ERROR.getMessage();

        JsonNode jsonNode;
        try (var body = response.body().asInputStream()) {
            jsonNode = new ObjectMapper().readValue(body, JsonNode.class);
        } catch (Exception e) {
            throw new CustomFeignException(response.status(), CLIENT_ERROR.getMessage());
        }

        if (jsonNode.has(MESSAGE.getValue())) {
            errorMessage = jsonNode.get(MESSAGE.getValue()).asText();
        }
        return new CustomFeignException(response.status(), errorMessage);
    }
}