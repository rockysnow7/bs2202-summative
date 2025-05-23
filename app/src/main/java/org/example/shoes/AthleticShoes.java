package org.example.shoes;

import java.time.LocalDate;
import org.example.settings.RestockSettings;
import org.example.enums.SoleType;
import org.example.enums.ClosureType;
import org.example.enums.HeelHeight;

/**
 * Represents a pair of athletic shoes.
 * @extends Shoes
 */
public class AthleticShoes extends Shoes {
    public String sport;

    public AthleticShoes(
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
        HeelHeight heelHeight,
        String sport
    ) {
        super(id, name, brand, size, color, material, dateLastBought, stockQuantity, cost, price, restockSettings, imagePath, soleType, closureType, heelHeight);
        this.sport = sport;
    }
}
