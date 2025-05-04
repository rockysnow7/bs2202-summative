package org.example.settings;

/**
 * Represents the restock settings for an item.
 */
public class RestockSettings {
    /**
     * The ID of the item.
     */
    public int itemId;
    /**
     * Whether the item should be restocked automatically.
     */
    public boolean restockAutomatically;
    /**
     * If the item should be restocked automatically, the minimum stock quantity to restock to.
     */
    public int minimumStockQuantity;

    public RestockSettings(int itemId, boolean restockAutomatically, int minimumStockQuantity) {
        this.itemId = itemId;
        this.restockAutomatically = restockAutomatically;
        this.minimumStockQuantity = minimumStockQuantity;
    }
}
