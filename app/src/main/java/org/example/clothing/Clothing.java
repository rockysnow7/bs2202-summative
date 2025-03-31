package org.example.clothing;

import java.time.LocalDate;
import org.example.settings.RestockSettings;
import org.example.enums.FormalityLevel;

public abstract class Clothing {
    public int id;
    public String name;
    public String brand;
    public int size;
    public String color;
    public String material;
    public LocalDate dateLastBought;
    public int stockQuantity;
    public RestockSettings restockSettings;
    public String imagePath;

    public Clothing(
        int id,
        String name,
        String brand,
        int size,
        String color,
        String material,
        LocalDate dateLastBought,
        int stockQuantity,
        RestockSettings restockSettings,
        String imagePath
    ) {
        this.id = id;
        this.name = name;
        this.brand = brand;
        this.size = size;
        this.color = color;
        this.material = material;
        this.dateLastBought = dateLastBought;
        this.stockQuantity = stockQuantity;
        this.restockSettings = restockSettings;
        this.imagePath = imagePath;
    }

    public void buy(int quantity) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public boolean sell(int quantity) {
        if (quantity > this.stockQuantity) {
            return false;
        }

        throw new UnsupportedOperationException("Not implemented yet");
        // return true;
    }

    public abstract FormalityLevel getFormalityLevel();
}
