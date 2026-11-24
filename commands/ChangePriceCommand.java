package commands;

import facade.ShopFacade;

public class ChangePriceCommand extends Command {
    private final int productId;
    private final int newPrice;
    private Integer previousPrice;

    public ChangePriceCommand(ShopFacade facade, int productId, int newPrice, Integer previousPrice) {
        super(facade);
        this.productId = productId;
        this.newPrice = newPrice;
        this.previousPrice = previousPrice;
    }

    public ChangePriceCommand(ShopFacade facade, int productId, int newPrice) {
        this(facade, productId, newPrice, null);
    }

    public void setPreviousPrice(int previousPrice) {
        this.previousPrice = previousPrice;
    }

    @Override
    public String execute() {
        return facade.changePrice(productId, newPrice);
    }

    @Override
    public String undo() {
        if (previousPrice == null) {
            return "не удалось откатить цену товара #" + productId + ": не сохранена предыдущая цена";
        }
        return facade.changePrice(productId, previousPrice);
    }
}