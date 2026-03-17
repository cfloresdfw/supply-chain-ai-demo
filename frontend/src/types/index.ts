export interface InventoryEvent {
  eventId:     string
  timestamp:   string
  warehouseId: string
  sku:         string
  status:      'STOCK_OUT' | 'DELAY_DETECTED'
  qtyGap:      number
}

export interface ResolutionDTO {
  resolutionId:     string
  eventId:          string
  resolvedAt:       string
  sourceWarehouse:  string
  nearestHub:       string
  qtyTransferred:   number
  impactScore:      number
  justification:    string
  resolutionStatus: 'AGENT_RESOLVED' | 'HUMAN_REQUIRED'
}
