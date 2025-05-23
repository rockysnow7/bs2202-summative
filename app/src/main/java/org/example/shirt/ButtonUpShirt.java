package org.example.shirt;

import org.example.enums.CuffStyle;
import org.example.enums.NeckType;
import org.example.enums.SleeveType;
import org.example.settings.RestockSettings;

import java.time.LocalDate;

/**
 * Represents a button up shirt.
 * @extends Shirt
 */
public class ButtonUpShirt extends Shirt {
    public CuffStyle cuffStyle;

    public ButtonUpShirt(
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
        int numPockets,
        CuffStyle cuffStyle
    ) {
        super(id, name, brand, size, color, material, dateLastBought, stockQuantity, cost, price, restockSettings, imagePath, sleeveType, neckType, pattern, numPockets);
        this.cuffStyle = cuffStyle;
    }
}
