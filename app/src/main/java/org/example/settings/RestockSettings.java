package org.example.settings;

public class RestockSettings {
    private int itemId;
    private boolean restockAutomatically;
    private int minimumStockQuantity;

    public RestockSettings(int itemId, boolean restockAutomatically, int minimumStockQuantity) {
        this.itemId = itemId;
        this.restockAutomatically = restockAutomatically;
        this.minimumStockQuantity = minimumStockQuantity;
    }

    public void setOn() {
        this.restockAutomatically = true;
    }

    public void setOff() {
        this.restockAutomatically = false;
    }

    public void setMinimumStockQuantity(int minimumStockQuantity) {
        this.minimumStockQuantity = minimumStockQuantity;
    }
}
