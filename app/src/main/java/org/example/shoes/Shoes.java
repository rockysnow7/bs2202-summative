package org.example.shoes;

import java.time.LocalDate;
import org.example.settings.RestockSettings;
import org.example.enums.SoleType;
import org.example.enums.ClosureType;
import org.example.enums.HeelHeight;
import org.example.enums.FormalityLevel;
import org.example.clothing.Clothing;

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
        RestockSettings restockSettings,
        String imagePath,
        SoleType soleType,
        ClosureType closureType,
        HeelHeight heelHeight
    ) {
        super(id, name, brand, size, color, material, dateLastBought, stockQuantity, restockSettings, imagePath);

        this.soleType = soleType;
        this.closureType = closureType;
        this.heelHeight = heelHeight;
    }

    @Override
    public FormalityLevel getFormalityLevel() {
        if (this.closureType == ClosureType.LACES) {
            return FormalityLevel.FORMAL;
        }
        return FormalityLevel.CASUAL;
    }
}
