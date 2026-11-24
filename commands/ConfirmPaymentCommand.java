package commands;

import facade.ShopFacade;

public class ConfirmPaymentCommand extends Command {
    private final int orderId;

    public ConfirmPaymentCommand(ShopFacade facade, int orderId) {
        super(facade);
        this.orderId = orderId;
    }

    @Override
    public String execute() {
        return facade.confirmPayment(orderId);
    }
}
