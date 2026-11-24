package handlers;

import commands.ShowOrdersCommand;
import facade.ShopFacade;

public class ShowOrdersHandler extends Handler {
    public ShowOrdersHandler(ShopFacade facade) {
        super(facade);
    }

    @Override
    protected boolean canHandle(String input) {
        return "show orders".equalsIgnoreCase(input.trim());
    }

    @Override
    protected void process(String input) {
        facade.executeCommand(new ShowOrdersCommand(facade));
    }
}
