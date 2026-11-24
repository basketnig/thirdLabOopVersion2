package commands;

import facade.ShopFacade;

public abstract class Command {
    protected final ShopFacade facade;

    protected Command(ShopFacade facade) {
        this.facade = facade;
    }

    public abstract String execute();

    public String undo() {
        return "откат не поддерживается для команды: " + getClass().getSimpleName();
    }
}