package commands;

import facade.ShopFacade;
import java.util.List;

public class CreateOrderCommand extends Command {
    private final String customer;
    private final List<Integer> items;
    private final String deliveryType;

    public CreateOrderCommand(ShopFacade facade, String customer, List<Integer> items, String deliveryType) {
        super(facade);
        this.customer = customer;
        this.items = items;
        this.deliveryType = deliveryType;
    }

    @Override
    public String execute() {
        return facade.createOrder(customer, items, deliveryType);
    }
}