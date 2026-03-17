package com.supplychainai.service;

import com.supplychainai.model.InventoryEvent;
import com.supplychainai.model.ResolutionDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

/**
 * Simulates incoming supply chain signals from the Temple, TX distribution center.
 * Fires every 15 seconds and triggers the agent reasoning loop.
 */
@Service
public class InventoryEventProducer {

    private static final Logger log = LoggerFactory.getLogger(InventoryEventProducer.class);

    private static final String WAREHOUSE_ID = "TEMPLE_TX_DIST";
    private static final String[] SKUS = {
        "MCL-990-DRINK", "MCL-102-SNACK", "MCL-445-DAIRY",
        "MCL-871-FROZEN", "MCL-336-BAKERY", "MCL-774-PAPER"
    };
    // Weighted toward STOCK_OUT (75%) vs DELAY_DETECTED (25%)
    private static final String[] STATUSES = {
        "STOCK_OUT", "STOCK_OUT", "STOCK_OUT", "DELAY_DETECTED"
    };

    private final SseEmitterService sseEmitterService;
    private final ReasoningService reasoningService;
    private final Random random = new Random();

    public InventoryEventProducer(SseEmitterService sseEmitterService,
                                  ReasoningService reasoningService) {
        this.sseEmitterService = sseEmitterService;
        this.reasoningService = reasoningService;
    }

    @Scheduled(fixedDelay = 15_000)
    public void produceEvent() {
        InventoryEvent event = new InventoryEvent(
                UUID.randomUUID().toString().substring(0, 8).toUpperCase(),
                LocalDateTime.now(),
                WAREHOUSE_ID,
                SKUS[random.nextInt(SKUS.length)],
                STATUSES[random.nextInt(STATUSES.length)],
                200 + random.nextInt(601)   // qty gap: 200 – 800 units
        );

        log.info("► Event [{}] {} — {} — gap: {} units",
                event.getEventId(), event.getStatus(), event.getSku(), event.getQtyGap());

        ResolutionDTO resolution = reasoningService.processEvent(event);

        log.info("  Resolution [{}] {} → {}",
                resolution.getResolutionId(), resolution.getResolutionStatus(), resolution.getNearestHub());

        sseEmitterService.broadcast(event, resolution);
    }
}
