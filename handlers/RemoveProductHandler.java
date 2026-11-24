package handlers;

import commands.RemoveProductCommand;
import facade.ShopFacade;

public class RemoveProductHandler extends Handler {
    public RemoveProductHandler(ShopFacade facade) {
        super(facade);
    }

    @Override
    protected boolean canHandle(String input) {
        return input.toLowerCase().startsWith("remove product ");
    }

    @Override
    protected void process(String input) {
        String[] parts = input.trim().split("\\s+");
        if (parts.length < 3) {
            System.out.println("формат: remove product <productId>");
            return;
        }
        try {
            int productId = Integer.parseInt(parts[2]);
            facade.executeCommand(new RemoveProductCommand(facade, productId));
        } catch (NumberFormatException e) {
            System.out.println("ошибка: ID товара должен быть целым числом");
        }
    }
}