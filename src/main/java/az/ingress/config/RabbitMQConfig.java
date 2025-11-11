package az.ingress.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static az.ingress.config.RabbitMQConstants.ORDER_EVENT;
import static az.ingress.config.RabbitMQConstants.ORDER_EVENT_DLQ;
import static az.ingress.config.RabbitMQConstants.ORDER_EXCHANGE;

@Configuration
@Slf4j
public class RabbitMQConfig {

    @Bean
    public DirectExchange orderExchange() {
        return new DirectExchange(ORDER_EXCHANGE);
    }

    @Bean
    public Queue orderQueue() {
        return QueueBuilder.durable(ORDER_EVENT)
                .withArgument("x-dead-letter-exchange", ORDER_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", ORDER_EVENT_DLQ)
                .build();
    }

    @Bean
    public Queue orderDLQ() {
        return QueueBuilder.durable(ORDER_EVENT_DLQ).build();
    }

    @Bean
    public Binding orderBinding(Queue orderQueue, DirectExchange orderExchange) {
        return BindingBuilder.bind(orderQueue).to(orderExchange).with(ORDER_EVENT);
    }

    @Bean
    public Binding orderDLQBinding(Queue orderDLQ, DirectExchange orderExchange) {
        return BindingBuilder.bind(orderDLQ).to(orderExchange).with(ORDER_EVENT_DLQ);
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        return new Jackson2JsonMessageConverter(mapper);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
                                         Jackson2JsonMessageConverter converter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(converter);

        template.setConfirmCallback((correlationData, ack, cause) -> {
            if (ack) {
                log.info("Message confirmed by broker: {}", correlationData);
            } else {
                log.error("Message not confirmed by broker: {}, cause: {}", correlationData, cause);
            }
        });

        template.setReturnsCallback(returned -> {
            log.error("Message returned: replyCode={}, replyText={}, exchange={}, routingKey={}, body={}",
                    returned.getReplyCode(),
                    returned.getReplyText(),
                    returned.getExchange(),
                    returned.getRoutingKey(),
                    new String(returned.getMessage().getBody()));
        });
        return template;
    }
}
