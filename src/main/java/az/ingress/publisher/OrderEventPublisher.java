package az.ingress.publisher;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import static az.ingress.config.RabbitMQConstants.ORDER_EVENT;
import static az.ingress.config.RabbitMQConstants.ORDER_EXCHANGE;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public void publish(Object event) {
        try {
            rabbitTemplate.convertAndSend(ORDER_EXCHANGE, ORDER_EVENT, event);
            log.info("Event published to exchange='{}' with routingKey='{}': {}",
                    ORDER_EXCHANGE, ORDER_EVENT, event);
        } catch (Exception ex) {
            log.error("Failed to publish event to RabbitMQ: {}", ex.getMessage(), ex);
        }
    }
}
