package az.ingress.service;

import az.ingress.dao.entity.OrderEntity;
import az.ingress.dao.entity.OrderOutboxEntity;
import az.ingress.dao.entity.OrderStatusEventEntity;
import az.ingress.dao.repository.OrderOutboxRepository;
import az.ingress.dao.repository.OrderStatusEventRepository;
import az.ingress.publisher.OrderEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OutboxEventService {

    private final OrderOutboxRepository outboxRepository;
    private final OrderEventPublisher orderEventPublisher;
    private final OrderStatusEventRepository orderStatusEventRepository;

    @Async
    @Transactional
    public void sendOrderToRecommendationMs() {
        var events = outboxRepository.findUnprocessedEvents();

        for (OrderOutboxEntity event : events) {
            try {
                orderEventPublisher.publish(event.getEvent());
                event.setProcessed(true);
                outboxRepository.save(event);
            } catch (Exception e) {
                System.err.println("Kafka send failed for eventId=" + event.getId() + ": " + e.getMessage());
            }
        }
    }

    @Async
    @Transactional
    public void deleteOutboxEvent() {
        var processedEvents = outboxRepository.findProcessedEvents();
        outboxRepository.deleteAll(processedEvents);
    }

    public void saveToOutbox(OrderEntity entity, List<String> categories) {
        try {
            var orderStatusEventEntity = orderStatusEventRepository.save(OrderStatusEventEntity.builder()
                    .categories(categories)
                    .createdAt(entity.getCreatedAt())
                    .userId(entity.getUserId())
                    .build());
            var event = OrderOutboxEntity.builder()
                    .eventType(entity.getStatus().name())
                    .event(orderStatusEventEntity)
                    .build();
            outboxRepository.save(event);
        } catch (Exception e) {
            throw new RuntimeException("Failed to save outbox event", e);
        }
    }
}
