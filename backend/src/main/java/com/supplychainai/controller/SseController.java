package com.supplychainai.controller;

import com.supplychainai.model.WarehouseStock;
import com.supplychainai.service.SseEmitterService;
import com.supplychainai.service.WarehouseService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@RestController
@RequestMapping("/api")
public class SseController {

    private final SseEmitterService sseEmitterService;
    private final WarehouseService warehouseService;

    public SseController(SseEmitterService sseEmitterService, WarehouseService warehouseService) {
        this.sseEmitterService = sseEmitterService;
        this.warehouseService = warehouseService;
    }

    /**
     * SSE endpoint — Next.js dashboard subscribes here for real-time events.
     */
    @GetMapping(value = "/events/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamEvents() {
        return sseEmitterService.createEmitter();
    }

    @GetMapping("/warehouses")
    public List<WarehouseStock> getWarehouses() {
        return warehouseService.getAllHubs();
    }

    @GetMapping("/health")
    public String health() {
        return "{\"status\":\"UP\",\"activeConnections\":" + sseEmitterService.getActiveConnectionCount() + "}";
    }
}
