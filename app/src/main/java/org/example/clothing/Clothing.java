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
    public double cost;
    public double price;
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
        double cost,
        double price,
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
        this.cost = cost;
        this.price = price;
        this.restockSettings = restockSettings;
        this.imagePath = imagePath;
    }

    public abstract FormalityLevel getFormalityLevel();
}
