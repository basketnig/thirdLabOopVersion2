package handlers;

import commands.ChangePriceCommand;
import facade.ShopFacade;

public class ChangePriceHandler extends Handler {
    public ChangePriceHandler(ShopFacade facade) {
        super(facade);
    }

    @Override
    protected boolean canHandle(String input) {
        return input.toLowerCase().startsWith("change price ");
    }

    @Override
    protected void process(String input) {
        String[] parts = input.trim().split("\\s+");
        if (parts.length < 4) {
            System.out.println("формат: change price <productId> <newPrice>");
            return;
        }
        try {
            int id = Integer.parseInt(parts[2]);
            int newPrice = Integer.parseInt(parts[3]);
            facade.executeCommand(new ChangePriceCommand(facade, id, newPrice));
        } catch (NumberFormatException e) {
            System.out.println("ошибка: ID товара и новая цена должны быть целыми числами");
        }
    }
}