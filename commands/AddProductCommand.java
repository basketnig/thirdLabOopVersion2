package commands;

import facade.ShopFacade;

public class AddProductCommand extends Command {
    private final String type;
    private final String name;
    private final int price;
    private final int quantity;

    public AddProductCommand(ShopFacade facade, String type, String name, int price, int quantity) {
        super(facade);
        this.type = type;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    @Override
    public String execute() {
        return facade.addProduct(type, name, price, quantity);
    }
}
