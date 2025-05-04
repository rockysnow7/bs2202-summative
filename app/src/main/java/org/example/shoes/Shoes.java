package org.example.shoes;

import java.time.LocalDate;
import org.example.settings.RestockSettings;
import org.example.enums.SoleType;
import org.example.enums.ClosureType;
import org.example.enums.HeelHeight;
import org.example.clothing.Clothing;

/**
 * Represents a generic pair of shoes.
 * @extends Clothing
 */
public abstract class Shoes extends Clothing {
    public SoleType soleType;
    public ClosureType closureType;
    public HeelHeight heelHeight;

    public Shoes(
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
        SoleType soleType,
        ClosureType closureType,
        HeelHeight heelHeight
    ) {
        super(id, name, brand, size, color, material, dateLastBought, stockQuantity, cost, price, restockSettings, imagePath);

        this.soleType = soleType;
        this.closureType = closureType;
        this.heelHeight = heelHeight;
    }
}
