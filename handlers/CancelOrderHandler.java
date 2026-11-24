package handlers;

import commands.CancelOrderCommand;
import facade.ShopFacade;

public class CancelOrderHandler extends Handler {
    public CancelOrderHandler(ShopFacade facade) {
        super(facade);
    }

    @Override
    protected boolean canHandle(String input) {
        return input.toLowerCase().startsWith("cancel order ");
    }

    @Override
    protected void process(String input) {
        String[] parts = input.trim().split("\\s+");
        if (parts.length < 3) {
            System.out.println("формат: cancel order <orderId>");
            return;
        }
        try {
            int orderId = Integer.parseInt(parts[2]);
            facade.executeCommand(new CancelOrderCommand(facade, orderId));
        } catch (NumberFormatException e) {
            System.out.println("ошибка: ID заказа должен быть целым числом");
        }
    }
}