package handlers;

import commands.ShowProductsCommand;
import facade.ShopFacade;

public class ShowProductsHandler extends Handler {
    public ShowProductsHandler(ShopFacade facade) {
        super(facade);
    }

    @Override
    protected boolean canHandle(String input) {
        return "show products".equalsIgnoreCase(input.trim());
    }

    @Override
    protected void process(String input) {
        facade.executeCommand(new ShowProductsCommand(facade));
    }
}
