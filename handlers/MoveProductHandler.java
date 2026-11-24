package handlers;

import commands.MoveProductCommand;
import facade.ShopFacade;

public class MoveProductHandler extends Handler {
    public MoveProductHandler(ShopFacade facade) {
        super(facade);
    }

    @Override
    protected boolean canHandle(String input) {
        return input.toLowerCase().startsWith("move product ");
    }

    @Override
    protected void process(String input) {
        String[] parts = input.trim().split("\\s+");
        if (parts.length < 5 || !"to".equalsIgnoreCase(parts[3])) {
            System.out.println("формат: move product <productId> to <targetLocation>");
            return;
        }
        try {
            int productId = Integer.parseInt(parts[2]);
            String target = parts[4];
            facade.executeCommand(new MoveProductCommand(facade, productId, target));
        } catch (NumberFormatException e) {
            System.out.println("ошибка: ID товара должен быть целым числом");
        }
    }
}