package products;

import attributes.Price;
import attributes.Quantity;
import attributes.Weight;
import warehouse.Warehouse;


public abstract class Product implements Cloneable {
    private static int nextId = 1;

    private final int id;
    private String name;
    private Price price;
    private Quantity quantity;
    private Weight weight;
    private final String category;
    private final ProductType type;

    public Product(String name, int priceVal, int quantityVal, double weightVal, String category) {
        this.id = nextId++;
        this.name = name;
        this.price = new Price(priceVal);
        this.quantity = new Quantity(quantityVal);
        this.weight = new Weight(weightVal);
        this.category = category;
        this.type = ProductType.getType(category, weightVal, priceVal);
    }

    protected Product(int id, String name, int priceVal, int quantityVal, double weightVal, String category) {
        this.id = id;
        this.name = name;
        this.price = new Price(priceVal);
        this.quantity = new Quantity(quantityVal);
        this.weight = new Weight(weightVal);
        this.category = category;
        this.type = ProductType.getType(category, weightVal, priceVal);
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public ProductType getType() { return type; }

    public Price getPrice() { return price; }
    public void setPrice(Price newPrice) { this.price = newPrice; }

    public Quantity getQuantity() { return quantity; }
    public void changeQuantity(int delta) { this.quantity.change(delta); }

    public Weight getWeight() { return weight; }

    public int getTotalPrice() {
        return price.getValue() * quantity.getValue();
    }

    public double getTotalWeight() {
        return weight.getValue() * quantity.getValue();
    }

    public void transferTo(Warehouse other) {
        other.addProduct(this);
    }

    @Override
    public abstract Product clone();

    @Override
    public String toString() {
        return String.format("id: %d, название: %s, категория: %s, цена: %d руб, колво: %d шт.",
                id, name, category, price.getValue(), quantity.getValue());
    }
}
