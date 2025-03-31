package org.example.shoes;

import org.example.enums.ClosureType;
import org.example.enums.HeelHeight;
import org.example.enums.SoleType;
import org.example.enums.ToeStyle;
import org.example.settings.RestockSettings;

import java.time.LocalDate;

public class DressShoes extends Shoes {
    public ToeStyle toeStyle;

    public DressShoes(
        int id,
        String name,
        String brand,
        int size,
        String color,
        String material,
        LocalDate dateLastBought,
        int stockQuantity,
        RestockSettings restockSettings,
        String imagePath,
        SoleType soleType,
        ClosureType closureType,
        HeelHeight heelHeight,
        ToeStyle toeStyle
    ) {
        super(id, name, brand, size, color, material, dateLastBought, stockQuantity, restockSettings, imagePath, soleType, closureType, heelHeight);
        this.toeStyle = toeStyle;
    }
}
