package commands;

import facade.ShopFacade;

public class RemoveProductCommand extends Command {
    private final int productId;

    public RemoveProductCommand(ShopFacade facade, int productId) {
        super(facade);
        this.productId = productId;
    }

    @Override
    public String execute() {
        return facade.removeProduct(productId);
    }
}
