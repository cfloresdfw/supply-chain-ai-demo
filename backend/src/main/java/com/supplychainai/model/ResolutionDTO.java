package com.supplychainai.model;

import java.time.LocalDateTime;

public class ResolutionDTO {

    private String resolutionId;
    private String eventId;
    private LocalDateTime resolvedAt;
    private String sourceWarehouse;
    private String nearestHub;
    private Integer qtyTransferred;
    private Integer impactScore;      // number of customer orders at risk
    private String justification;     // simulated LLM reasoning
    private String resolutionStatus;  // "AGENT_RESOLVED" or "HUMAN_REQUIRED"

    public ResolutionDTO() {}

    public String getResolutionId() { return resolutionId; }
    public void setResolutionId(String resolutionId) { this.resolutionId = resolutionId; }

    public String getEventId() { return eventId; }
    public void setEventId(String eventId) { this.eventId = eventId; }

    public LocalDateTime getResolvedAt() { return resolvedAt; }
    public void setResolvedAt(LocalDateTime resolvedAt) { this.resolvedAt = resolvedAt; }

    public String getSourceWarehouse() { return sourceWarehouse; }
    public void setSourceWarehouse(String sourceWarehouse) { this.sourceWarehouse = sourceWarehouse; }

    public String getNearestHub() { return nearestHub; }
    public void setNearestHub(String nearestHub) { this.nearestHub = nearestHub; }

    public Integer getQtyTransferred() { return qtyTransferred; }
    public void setQtyTransferred(Integer qtyTransferred) { this.qtyTransferred = qtyTransferred; }

    public Integer getImpactScore() { return impactScore; }
    public void setImpactScore(Integer impactScore) { this.impactScore = impactScore; }

    public String getJustification() { return justification; }
    public void setJustification(String justification) { this.justification = justification; }

    public String getResolutionStatus() { return resolutionStatus; }
    public void setResolutionStatus(String resolutionStatus) { this.resolutionStatus = resolutionStatus; }
}
