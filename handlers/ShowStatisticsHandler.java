package handlers;

import commands.ShowStatisticsCommand;
import facade.ShopFacade;

public class ShowStatisticsHandler extends Handler {
    public ShowStatisticsHandler(ShopFacade facade) {
        super(facade);
    }

    @Override
    protected boolean canHandle(String input) {
        return "show statistics".equalsIgnoreCase(input.trim());
    }

    @Override
    protected void process(String input) {
        facade.executeCommand(new ShowStatisticsCommand(facade));
    }
}
