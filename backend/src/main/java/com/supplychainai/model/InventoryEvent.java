package com.supplychainai.model;

import java.time.LocalDateTime;

public class InventoryEvent {

    private String eventId;
    private LocalDateTime timestamp;
    private String warehouseId;  // e.g., "TEMPLE_TX_DIST"
    private String sku;          // e.g., "MCL-990-DRINK"
    private String status;       // "STOCK_OUT" or "DELAY_DETECTED"
    private Integer qtyGap;      // quantity missing

    public InventoryEvent() {}

    public InventoryEvent(String eventId, LocalDateTime timestamp, String warehouseId,
                          String sku, String status, Integer qtyGap) {
        this.eventId = eventId;
        this.timestamp = timestamp;
        this.warehouseId = warehouseId;
        this.sku = sku;
        this.status = status;
        this.qtyGap = qtyGap;
    }

    public String getEventId() { return eventId; }
    public void setEventId(String eventId) { this.eventId = eventId; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public String getWarehouseId() { return warehouseId; }
    public void setWarehouseId(String warehouseId) { this.warehouseId = warehouseId; }

    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Integer getQtyGap() { return qtyGap; }
    public void setQtyGap(Integer qtyGap) { this.qtyGap = qtyGap; }
}
