package products;

import java.util.HashMap;
import java.util.Map;

public class ProductType {
    private static final Map<String, ProductType> pool = new HashMap<>();

    private final String category;
    private final double baseWeight;
    private final int basePrice;

    private ProductType(String category, double baseWeight, int basePrice) {
        this.category = category;
        this.baseWeight = baseWeight;
        this.basePrice = basePrice;
    }

    public static ProductType getType(String category, double baseWeight, int basePrice) {
        return pool.computeIfAbsent(category, k -> new ProductType(category, baseWeight, basePrice));
    }

    public static ProductType getType(String category) {
        return pool.computeIfAbsent(category, k -> new ProductType(category, 0.5, 100));
    }

    public String getCategory() { return category; }
    public double getBaseWeight() { return baseWeight; }
    public int getBasePrice() { return basePrice; }
    public static int getPoolSize() { return pool.size(); }
}
