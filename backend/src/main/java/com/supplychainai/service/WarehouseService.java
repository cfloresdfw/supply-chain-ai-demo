package com.supplychainai.service;

import com.supplychainai.model.WarehouseStock;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Mock warehouse inventory service. Holds secondary hub stock levels
 * for the Temple, TX distribution network.
 */
@Service
public class WarehouseService {

    // Sorted by distance from Temple, TX
    private final List<WarehouseStock> hubs;
    private final Random random = new Random();

    public WarehouseService() {
        hubs = new ArrayList<>();
        hubs.add(new WarehouseStock("DALLAS_HUB",       "Dallas Distribution Center",    "Dallas, TX",       2200, 62.0));
        hubs.add(new WarehouseStock("AUSTIN_HUB",       "Austin Logistics Hub",          "Austin, TX",       1800, 68.0));
        hubs.add(new WarehouseStock("SAN_ANTONIO_HUB",  "San Antonio Regional Hub",      "San Antonio, TX",  3500, 145.0));
        hubs.add(new WarehouseStock("HOUSTON_HUB",      "Houston Distribution Center",   "Houston, TX",      5000, 183.0));
    }

    /**
     * Finds the nearest hub that has at least {@code requiredQty} units in stock.
     * Randomly simulates a fully-depleted-network scenario (20% chance) to trigger
     * the HUMAN_REQUIRED escalation path.
     */
    public Optional<WarehouseStock> findNearestHubWithStock(int requiredQty) {
        // 20% chance: simulate all hubs exhausted → escalation scenario
        if (random.nextInt(10) < 2) {
            return Optional.empty();
        }

        return hubs.stream()
                .filter(h -> h.getAvailableStock() >= requiredQty)
                .min(Comparator.comparingDouble(WarehouseStock::getDistanceMiles));
    }

    public List<WarehouseStock> getAllHubs() {
        return Collections.unmodifiableList(hubs);
    }
}
