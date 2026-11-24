package handlers;

import commands.ShipOrderCommand;
import facade.ShopFacade;

public class ShipOrderHandler extends Handler {
    public ShipOrderHandler(ShopFacade facade) {
        super(facade);
    }

    @Override
    protected boolean canHandle(String input) {
        return input.toLowerCase().startsWith("ship order ");
    }

    @Override
    protected void process(String input) {
        String[] parts = input.trim().split("\\s+");
        if (parts.length < 3) {
            System.out.println("формат: ship order <orderId>");
            return;
        }
        try {
            int orderId = Integer.parseInt(parts[2]);
            facade.executeCommand(new ShipOrderCommand(facade, orderId));
        } catch (NumberFormatException e) {
            System.out.println("ошибка: ID заказа должен быть целым числом");
        }
    }
}