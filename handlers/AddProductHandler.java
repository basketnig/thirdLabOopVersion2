package handlers;

import commands.AddProductCommand;
import facade.ShopFacade;
import java.util.List;

public class AddProductHandler extends Handler {
    public AddProductHandler(ShopFacade facade) {
        super(facade);
    }

    @Override
    protected boolean canHandle(String input) {
        return input.toLowerCase().startsWith("add product ");
    }

    @Override
    protected void process(String input) {
        List<String> parts = tokenizeWithQuotes(input);
        if (parts.size() < 6) {
            System.out.println("формат: add product <Type> \"<Name>\" <price> <quantity>");
            return;
        }
        try {
            String type = parts.get(2);
            String name = parts.get(3);
            int price = Integer.parseInt(parts.get(4));
            int quantity = Integer.parseInt(parts.get(5));
            facade.executeCommand(new AddProductCommand(facade, type, name, price, quantity));
        } catch (NumberFormatException e) {
            System.out.println("ошибка: цена и количество должны быть целыми числами");
        }
    }
}