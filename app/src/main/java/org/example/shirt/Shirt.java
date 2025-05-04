package org.example.shirt;

import java.time.LocalDate;
import org.example.settings.RestockSettings;
import org.example.enums.SleeveType;
import org.example.enums.NeckType;
import org.example.clothing.Clothing;

/**
 * Represents a generic shirt.
 * @extends Clothing
 */
public abstract class Shirt extends Clothing {
    public SleeveType sleeveType;
    public NeckType neckType;
    public String pattern;
    public int numPockets;

    public Shirt(
        int id,
        String name,
        String brand,
        int size,
        String color,
        String material,
        LocalDate dateLastBought,
        int stockQuantity,
        double cost,
        double price,
        RestockSettings restockSettings,
        String imagePath,
        SleeveType sleeveType,
        NeckType neckType,
        String pattern,
        int numPockets
    ) {
        super(id, name, brand, size, color, material, dateLastBought, stockQuantity, cost, price, restockSettings, imagePath);

        this.sleeveType = sleeveType;
        this.neckType = neckType;
        this.pattern = pattern;
        this.numPockets = numPockets;
    }
}
