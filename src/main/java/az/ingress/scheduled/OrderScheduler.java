package az.ingress.scheduled;

import az.ingress.service.OutboxEventService;
import lombok.RequiredArgsConstructor;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderScheduler {

    private final OutboxEventService outboxEventService;

    @Scheduled(fixedDelayString = "PT5M")
    @SchedulerLock(name = "sendOrderEvent",
            lockAtLeastFor = "PT1M",
            lockAtMostFor = "PT4M")
    public void sendOrderEvent() {
        outboxEventService.sendOrderToRecommendationMs();
    }


    @Scheduled(fixedDelayString = "PT10H")
    @SchedulerLock(name = "sendOrder",
            lockAtLeastFor = "PT1M",
            lockAtMostFor = "PT4M")
    public void deleteOutboxEvent() {
        outboxEventService.deleteOutboxEvent();
    }
}
