package com.supplychainai.service;

import com.supplychainai.model.InventoryEvent;
import com.supplychainai.model.ResolutionDTO;
import com.supplychainai.model.WarehouseStock;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

/**
 * Simulates an AI reasoning loop ("tool-use" agent).
 *
 * Steps:
 *  1. Receive an InventoryEvent
 *  2. Query WarehouseService for the nearest hub with sufficient stock
 *  3. Calculate an Impact Score (orders affected)
 *  4. Produce a ResolutionDTO with justification text (simulated LLM output)
 */
@Service
public class ReasoningService {

    private final WarehouseService warehouseService;

    public ReasoningService(WarehouseService warehouseService) {
        this.warehouseService = warehouseService;
    }

    public ResolutionDTO processEvent(InventoryEvent event) {
        ResolutionDTO resolution = new ResolutionDTO();
        resolution.setResolutionId(UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        resolution.setEventId(event.getEventId());
        resolution.setResolvedAt(LocalDateTime.now());
        resolution.setSourceWarehouse(event.getWarehouseId());

        int impactScore = calculateImpactScore(event.getQtyGap());
        resolution.setImpactScore(impactScore);

        Optional<WarehouseStock> hub = warehouseService.findNearestHubWithStock(event.getQtyGap());

        if (hub.isPresent()) {
            WarehouseStock selectedHub = hub.get();
            resolution.setNearestHub(selectedHub.getWarehouseId());
            resolution.setQtyTransferred(event.getQtyGap());
            resolution.setResolutionStatus("AGENT_RESOLVED");
            resolution.setJustification(buildAgentJustification(selectedHub, event.getQtyGap(), impactScore));
        } else {
            resolution.setNearestHub("N/A");
            resolution.setQtyTransferred(0);
            resolution.setResolutionStatus("HUMAN_REQUIRED");
            resolution.setJustification(buildEscalationJustification(event.getQtyGap(), impactScore));
        }

        return resolution;
    }

    // ~0.3 downstream orders impacted per missing unit
    private int calculateImpactScore(int qtyGap) {
        return (int) Math.round(qtyGap * 0.3);
    }

    private String buildAgentJustification(WarehouseStock hub, int qty, int impactScore) {
        int idx = (int) (Math.random() * 3);
        return switch (idx) {
            case 0 -> String.format(
                    "Found surplus at %s; initiated %d-unit transfer to protect OTIF for %d local retailers.",
                    hub.getCity(), qty, impactScore);
            case 1 -> String.format(
                    "Stock analysis complete. %s maintains %d units on-hand. Rerouting %d units to Temple TX ensures continuity for %d downstream customers.",
                    hub.getWarehouseId(), hub.getAvailableStock(), qty, impactScore);
            default -> String.format(
                    "Critical shortage detected at TEMPLE_TX_DIST. Nearest qualified hub (%s, %.0f mi) selected for emergency resupply of %d units, protecting %d active orders.",
                    hub.getCity(), hub.getDistanceMiles(), qty, impactScore);
        };
    }

    private String buildEscalationJustification(int qtyGap, int impactScore) {
        return String.format(
                "All alternative hubs have insufficient inventory for the required %d-unit fulfillment. " +
                "Escalating to procurement for emergency vendor order. " +
                "Impact: %d at-risk customer orders require manual coordination.",
                qtyGap, impactScore);
    }
}
