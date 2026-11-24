package commands;

import facade.ShopFacade;

public class CancelOrderCommand extends Command {
    private final int orderId;

    public CancelOrderCommand(ShopFacade facade, int orderId) {
        super(facade);
        this.orderId = orderId;
    }

    @Override
    public String execute() {
        return facade.cancelOrder(orderId);
    }
}
