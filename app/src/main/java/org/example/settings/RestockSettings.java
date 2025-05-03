package org.example.settings;

public class RestockSettings {
    public int itemId;
    public boolean restockAutomatically;
    public int minimumStockQuantity;

    public RestockSettings(int itemId, boolean restockAutomatically, int minimumStockQuantity) {
        this.itemId = itemId;
        this.restockAutomatically = restockAutomatically;
        this.minimumStockQuantity = minimumStockQuantity;
    }
}
