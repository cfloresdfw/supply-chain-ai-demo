package com.supplychainai.service;

import com.supplychainai.model.InventoryEvent;
import com.supplychainai.model.ResolutionDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Manages SSE emitter lifecycle and broadcasts supply chain events
 * to all connected frontend clients.
 */
@Service
public class SseEmitterService {

    private static final Logger log = LoggerFactory.getLogger(SseEmitterService.class);

    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    public SseEmitter createEmitter() {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);

        emitter.onCompletion(() -> {
            emitters.remove(emitter);
            log.debug("SSE client disconnected. Active: {}", emitters.size());
        });
        emitter.onTimeout(() -> {
            emitters.remove(emitter);
            log.debug("SSE timeout. Active: {}", emitters.size());
        });
        emitter.onError(e -> {
            emitters.remove(emitter);
            log.debug("SSE error: {}. Active: {}", e.getMessage(), emitters.size());
        });

        emitters.add(emitter);
        log.info("New SSE client connected. Active connections: {}", emitters.size());

        // Immediately confirm connection to the client
        try {
            emitter.send(SseEmitter.event()
                    .name("connected")
                    .data("{\"message\":\"Connected to Supply Chain Control Tower\",\"timestamp\":\"" + LocalDateTime.now() + "\"}"));
        } catch (IOException e) {
            emitters.remove(emitter);
        }

        return emitter;
    }

    /**
     * Pushes both the raw InventoryEvent and its AI-generated ResolutionDTO
     * to every connected client. Dead emitters are cleaned up automatically.
     */
    public void broadcast(InventoryEvent event, ResolutionDTO resolution) {
        List<SseEmitter> dead = new ArrayList<>();

        for (SseEmitter emitter : emitters) {
            try {
                emitter.send(SseEmitter.event().name("inventory-event").data(event));
                emitter.send(SseEmitter.event().name("resolution").data(resolution));
            } catch (IOException e) {
                dead.add(emitter);
                log.warn("Dead emitter removed: {}", e.getMessage());
            }
        }

        emitters.removeAll(dead);
    }

    public int getActiveConnectionCount() {
        return emitters.size();
    }
}
