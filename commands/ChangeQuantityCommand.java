package commands;

import facade.ShopFacade;

public class ChangeQuantityCommand extends Command {
    private final int productId;
    private final int delta;

    public ChangeQuantityCommand(ShopFacade facade, int productId, int delta) {
        super(facade);
        this.productId = productId;
        this.delta = delta;
    }

    @Override
    public String execute() {
        return facade.changeQuantity(productId, delta);
    }

    @Override
    public String undo() {
        return facade.changeQuantity(productId, -delta);
    }
}
