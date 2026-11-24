package handlers;

import facade.ShopFacade;

public class HelpHandler extends Handler {
    public HelpHandler(ShopFacade facade) {
        super(facade);
    }

    @Override
    protected boolean canHandle(String input) {
        return "help".equalsIgnoreCase(input.trim());
    }

    @Override
    protected void process(String input) {
        System.out.println("""
                Доступные команды:
                  add product <Type> "<Name>" <price> <quantity>
                  change price <productId> <newPrice>
                  change quantity <productId> <delta>
                  move product <productId> to <target>
                  remove product <productId>
                  create order "<Customer>" <id1,id2,...> <deliveryType>
                  cancel order <orderId>
                  confirm payment <orderId>
                  ship order <orderId>
                  show products
                  show orders
                  show statistics
                  help
                  exit
                """);
    }
}
