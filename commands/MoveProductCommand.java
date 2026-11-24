package commands;

import facade.ShopFacade;

public class MoveProductCommand extends Command {
    private final int productId;
    private final String newWarehouseId;

    public MoveProductCommand(ShopFacade facade, int productId, String newWarehouseId) {
        super(facade);
        this.productId = productId;
        this.newWarehouseId = newWarehouseId;
    }

    @Override
    public String execute() {
        return facade.moveProduct(productId, newWarehouseId);
    }
}
