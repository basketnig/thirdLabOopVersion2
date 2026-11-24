package handlers;

import commands.ChangeQuantityCommand;
import facade.ShopFacade;

public class ChangeQuantityHandler extends Handler {
    public ChangeQuantityHandler(ShopFacade facade) {
        super(facade);
    }

    @Override
    protected boolean canHandle(String input) {
        return input.toLowerCase().startsWith("change quantity ");
    }

    @Override
    protected void process(String input) {
        String[] parts = input.trim().split("\\s+");
        if (parts.length < 4) {
            System.out.println("формат: change quantity <productId> <delta>");
            return;
        }
        try {
            int productId = Integer.parseInt(parts[2]);
            int delta = Integer.parseInt(parts[3]);
            facade.executeCommand(new ChangeQuantityCommand(facade, productId, delta));
        } catch (NumberFormatException e) {
            System.out.println("ошибка: ID товара и изменение количества должны быть целыми числами");
        }
    }
}