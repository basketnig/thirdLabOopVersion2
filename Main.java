import commands.CommandHistory;
import facade.LoggingProxy;
import facade.ShopFacade;
import handlers.*;
import mediator.DeliveryService;
import mediator.PaymentSystem;
import mediator.ShopMediator;
import orders.OrderManager;
import warehouse.Warehouse;

import java.util.Scanner;

public class Main {
    private static ShopFacade buildFacade() {
        Warehouse warehouse = new Warehouse(50);
        OrderManager orderManager = new OrderManager(3, 20);
        PaymentSystem paymentSystem = new PaymentSystem();
        DeliveryService deliveryService = new DeliveryService();
        ShopMediator mediator = new ShopMediator(warehouse, orderManager, paymentSystem, deliveryService);
        CommandHistory history = new CommandHistory();
        LoggingProxy logger = new LoggingProxy();

        ShopFacade facade = new ShopFacade(warehouse, orderManager, mediator, history, logger);

        Handler head = new AddProductHandler(facade);
        head.setNext(new ChangePriceHandler(facade))
                .setNext(new ChangeQuantityHandler(facade))
                .setNext(new MoveProductHandler(facade))
                .setNext(new RemoveProductHandler(facade))
                .setNext(new CreateOrderHandler(facade))
                .setNext(new CancelOrderHandler(facade))
                .setNext(new ConfirmPaymentHandler(facade))
                .setNext(new ShipOrderHandler(facade))
                .setNext(new ShowProductsHandler(facade))
                .setNext(new ShowOrdersHandler(facade))
                .setNext(new ShowStatisticsHandler(facade))
                .setNext(new HelpHandler(facade));

        facade.setChainHead(head);
        return facade;
    }

    public static void main(String[] args) {
        ShopFacade facade = buildFacade();

        System.out.println("сценарий 1: добавление товаров и просмотр склада");
        facade.processInput("add product Smartphone \"iPhone 15\" 100000 5");
        facade.processInput("add product Laptop \"MacBook Pro\" 200000 3");
        facade.processInput("show products");

        System.out.println("\nсценарий 2: создание заказа с доставкой и оплатой ");
        facade.processInput("create order \"максим синицын\" 1,2 courier");
        facade.processInput("confirm payment 1");
        facade.processInput("show orders");

        System.out.println("\nсценарий 3: изменение цены и остатка товара");
        facade.processInput("change price 1 90000");
        facade.processInput("change quantity 1 -2");
        facade.processInput("show products");

        System.out.println("\nсценарий 4: создание второго заказа, отмена и отправка");
        facade.processInput("create order \"федос манчестео\" 1 pickup");
        facade.processInput("cancel order 2");
        facade.processInput("ship order 1");
        facade.processInput("show statistics");

        System.out.println("\nсценарий 5: координация через посредник");
        facade.processInput("create order \"марат матрецов\" 2 post");
        facade.processInput("remove product 2");
        facade.processInput("show statistics");

        System.out.println("\nсценарий 6: несколько позиций и откат при нехватке второй");
        facade.processInput("add product Milk \"молоко фермерское\" 90 1");
        facade.processInput("show products");

        facade.processInput("create order \"сашка\" 1,3,3 courier");
        facade.processInput("show products");
        facade.processInput("show statistics");

        System.out.println("\nсценарий 7: ошибки ввода, ID, повторные операции и недоступная доставка");

        facade.processInput("do something strange");

        facade.processInput("change price 1 abc");
        facade.processInput("add product Smartphone \"Broken\"");

        facade.processInput("change price 999 50000");
        facade.processInput("confirm payment 999");

        facade.processInput("create order \"Анна\" 1 pickup");
        facade.processInput("confirm payment 2");
        facade.processInput("confirm payment 2");
        facade.processInput("create order \"Олег\" 1 pickup");
        facade.processInput("cancel order 3");
        facade.processInput("cancel order 3");

        facade.processInput("add product Laptop \"Тяжёлый сервер\" 300000 2");
        facade.processInput("change quantity 4 24");
        facade.processInput("create order \"Сергей\" 4 post");

        System.out.println("\nввод пользователя(введите 'help' или 'exit')");
        try (Scanner scanner = new Scanner(System.in)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (line.equalsIgnoreCase("exit")) {
                    break;
                }
                if (!line.isEmpty()) {
                    facade.processInput(line);
                }
            }
        }
    }
}