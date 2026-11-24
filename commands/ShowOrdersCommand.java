package commands;

import facade.ShopFacade;

public class ShowOrdersCommand extends Command {
    public ShowOrdersCommand(ShopFacade facade) {
        super(facade);
    }

    @Override
    public String execute() {
        return facade.listOrders();
    }
}
