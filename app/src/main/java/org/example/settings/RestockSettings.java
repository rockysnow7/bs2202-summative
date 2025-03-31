package org.example.settings;

public class RestockSettings {
    private boolean restockAutomatically;
    private int minimumStockQuantity;

    public RestockSettings(boolean restockAutomatically, int minimumStockQuantity) {
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
