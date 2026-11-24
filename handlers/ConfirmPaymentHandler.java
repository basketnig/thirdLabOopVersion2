package handlers;

import commands.ConfirmPaymentCommand;
import facade.ShopFacade;

public class ConfirmPaymentHandler extends Handler {
    public ConfirmPaymentHandler(ShopFacade facade) {
        super(facade);
    }

    @Override
    protected boolean canHandle(String input) {
        return input.toLowerCase().startsWith("confirm payment ");
    }

    @Override
    protected void process(String input) {
        String[] parts = input.trim().split("\\s+");
        if (parts.length < 3) {
            System.out.println("формат: confirm payment <orderId>");
            return;
        }
        try {
            int orderId = Integer.parseInt(parts[2]);
            facade.executeCommand(new ConfirmPaymentCommand(facade, orderId));
        } catch (NumberFormatException e) {
            System.out.println("ошибка: ID заказа должен быть целым числом");
        }
    }
}