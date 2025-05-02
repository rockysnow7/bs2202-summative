package org.example.shirt;

import java.time.LocalDate;
import org.example.settings.RestockSettings;
import org.example.enums.SleeveType;
import org.example.enums.NeckType;

public class TShirt extends Shirt {
    public boolean hasGraphic;

    public TShirt(
        int id,
        String name,
        String brand,
        int size,
        String color,
        String material,
        LocalDate dateLastBought,
        int stockQuantity,
        double price,
        RestockSettings restockSettings,
        String imagePath,
        SleeveType sleeveType,
        NeckType neckType,
        String pattern,
        int numPockets,
        boolean hasGraphic
    ) {
        super(id, name, brand, size, color, material, dateLastBought, stockQuantity, price, restockSettings, imagePath, sleeveType, neckType, pattern, numPockets);
        this.hasGraphic = hasGraphic;
    }
}
