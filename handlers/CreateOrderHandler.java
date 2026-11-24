package handlers;

import commands.CreateOrderCommand;
import facade.ShopFacade;
import java.util.ArrayList;
import java.util.List;

public class CreateOrderHandler extends Handler {
    public CreateOrderHandler(ShopFacade facade) {
        super(facade);
    }

    @Override
    protected boolean canHandle(String input) {
        return input.toLowerCase().startsWith("create order ");
    }

    @Override
    protected void process(String input) {
        List<String> parts = tokenizeWithQuotes(input);
        if (parts.size() < 5) {
            System.out.println("формат: create order \"<Customer>\" <id1,id2,...> <deliveryType>");
            return;
        }
        try {
            String customer = parts.get(2);
            String[] rawIds = parts.get(3).split(",");
            List<Integer> productIds = new ArrayList<>();
            for (String rawId : rawIds) {
                String trimmedId = rawId.trim();
                if (!trimmedId.isEmpty()) {
                    productIds.add(Integer.parseInt(trimmedId));
                }
            }
            if (productIds.isEmpty()) {
                System.out.println("ошибка: укажите хотя бы один ID товара");
                return;
            }
            String deliveryType = parts.get(4);
            facade.executeCommand(new CreateOrderCommand(facade, customer, productIds, deliveryType));
        } catch (NumberFormatException e) {
            System.out.println("ошибка: ID товаров должны быть целыми числами через запятую");
        }
    }
}