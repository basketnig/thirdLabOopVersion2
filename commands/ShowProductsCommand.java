package commands;

import facade.ShopFacade;

public class ShowProductsCommand extends Command {
    public ShowProductsCommand(ShopFacade facade) {
        super(facade);
    }

    @Override
    public String execute() {
        return facade.listProducts();
    }
}
