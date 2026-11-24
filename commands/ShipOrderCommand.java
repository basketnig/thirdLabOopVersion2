package commands;

import facade.ShopFacade;

public class ShipOrderCommand extends Command {
    private final int orderId;

    public ShipOrderCommand(ShopFacade facade, int orderId) {
        super(facade);
        this.orderId = orderId;
    }

    @Override
    public String execute() {
        return facade.shipOrder(orderId);
    }
}
