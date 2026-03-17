package com.supplychainai.model;

public class WarehouseStock {

    private String warehouseId;
    private String name;
    private String city;
    private int availableStock;
    private double distanceMiles;

    public WarehouseStock() {}

    public WarehouseStock(String warehouseId, String name, String city,
                          int availableStock, double distanceMiles) {
        this.warehouseId = warehouseId;
        this.name = name;
        this.city = city;
        this.availableStock = availableStock;
        this.distanceMiles = distanceMiles;
    }

    public String getWarehouseId() { return warehouseId; }
    public void setWarehouseId(String warehouseId) { this.warehouseId = warehouseId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public int getAvailableStock() { return availableStock; }
    public void setAvailableStock(int availableStock) { this.availableStock = availableStock; }

    public double getDistanceMiles() { return distanceMiles; }
    public void setDistanceMiles(double distanceMiles) { this.distanceMiles = distanceMiles; }
}
