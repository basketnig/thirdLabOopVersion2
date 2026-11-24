package commands;

import facade.ShopFacade;

public class ShowStatisticsCommand extends Command {
    public ShowStatisticsCommand(ShopFacade facade) {
        super(facade);
    }

    @Override
    public String execute() {
        return facade.getStatistics();
    }
}
