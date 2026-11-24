package facade;

import attributes.Price;
import commands.Command;
import commands.CommandHistory;
import factories.*;
import handlers.Handler;
import mediator.ShopMediator;
import orders.Order;
import orders.OrderManager;
import products.Product;
import warehouse.Warehouse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShopFacade {
    private final Warehouse warehouse;
    private final OrderManager orderManager;
    private final ShopMediator mediator;
    private final CommandHistory history;
    private final LoggingProxy logger;
    private final Map<String, ProductFactory> factories = new HashMap<>();
    private Handler chainHead;

    public ShopFacade(Warehouse warehouse,
                      OrderManager orderManager,
                      ShopMediator mediator,
                      CommandHistory history,
                      LoggingProxy logger) {
        this.warehouse = warehouse;
        this.orderManager = orderManager;
        this.mediator = mediator;
        this.history = history;
        this.logger = logger;

        factories.put("smartphone", new SmartphoneFactory());
        factories.put("laptop", new LaptopFactory());
        factories.put("jacket", new JacketFactory());
        factories.put("bread", new BreadFactory());
        factories.put("milk", new MilkFactory());
        factories.put("tshirt", new TShirtFactory());
    }

    public void setChainHead(Handler chainHead) {
        this.chainHead = chainHead;
    }

    public String addProduct(String type, String name, int price, int quantity) {
        ProductFactory factory = factories.get(type.toLowerCase());
        if (factory == null) {
            throw new IllegalArgumentException("неизвестный тип товара: " + type);
        }
        Product product = factory.createProduct(name, price, quantity, 0.5);
        warehouse.addProduct(product);
        mediator.onProductAdded(product);
        return "товар добавлен: " + product;
    }

    public String changePrice(int productId, int newPrice) {
        Product product = warehouse.findById(productId);
        if (product == null) {
            throw new IllegalArgumentException("товар #" + productId + " не найден");
        }
        product.setPrice(new Price(newPrice));
        mediator.onProductChanged(productId, "новая цена = " + newPrice);
        return "цена товара #" + productId + " изменена на " + newPrice;
    }

    public String changeQuantity(int productId, int delta) {
        Product product = warehouse.findById(productId);
        if (product == null) {
            throw new IllegalArgumentException("товар #" + productId + " не найден");
        }
        product.changeQuantity(delta);
        mediator.onProductChanged(productId, "изменение количества на " + delta);
        return "количество товара #" + productId + " изменено на " + delta +
                " (остаток: " + product.getQuantity().getValue() + ")";
    }

    public String moveProduct(int productId, String targetLocation) {
        Product product = warehouse.findById(productId);
        if (product == null) {
            throw new IllegalArgumentException("товар #" + productId + " не найден");
        }
        mediator.onProductChanged(productId, "перемещён в " + targetLocation);
        return "товар #" + productId + " перемещён в '" + targetLocation + "'";
    }

    public String removeProduct(int productId) {
        mediator.onProductRemoved(productId);
        warehouse.removeById(productId);
        return "товар #" + productId + " удалён со склада";
    }

    public String listProducts() {
        StringBuilder sb = new StringBuilder("список товаров на складе:\n");
        boolean empty = true;
        for (Product p : warehouse) {
            empty = false;
            sb.append("  ").append(p).append("\n");
        }
        if (empty) {
            sb.append("  (склад пуст)\n");
        }
        return sb.toString().trim();
    }

    public String createOrder(String customer, List<Integer> productIds, String deliveryType) {
        Order order = mediator.createOrder(customer, productIds, deliveryType, "Москва");
        return "создан " + order;
    }

    public String cancelOrder(int orderId) {
        mediator.cancelOrder(orderId);
        return "заказ #" + orderId + " отменён";
    }

    public String confirmPayment(int orderId) {
        mediator.confirmPayment(orderId);
        return "оплата заказа #" + orderId + " подтверждена";
    }

    public String shipOrder(int orderId) {
        mediator.shipOrder(orderId);
        return "заказ #" + orderId + " отправлен";
    }

    public String listOrders() {
        StringBuilder sb = new StringBuilder("список заказов:\n");
        List<Order> orders = orderManager.getOrders();
        if (orders.isEmpty()) {
            sb.append("  (заказов нет)\n");
        } else {
            for (Order o : orders) {
                sb.append("  ").append(o).append("\n");
            }
        }
        return sb.toString().trim();
    }

    public String getStatistics() {
        return mediator.getStatistics();
    }

    public String executeCommand(Command command) {
        if (command == null) {
            return "команда не задана";
        }
        logger.logAction("COMMAND", "выполнение команды: " + command.getClass().getSimpleName());
        try {
            String result = command.execute();
            history.push(command);
            logger.logResult("COMMAND", result);
            return result;
        } catch (RuntimeException e) {
            logger.logError("COMMAND", e.getMessage());
            return "ошибка: " + e.getMessage();
        }
    }

    public String processInput(String input) {
        logger.logAction("FACADE", "ввод пользователя: " + input);
        if (chainHead == null) {
            return "цепочка обработчиков не настроена";
        }
        boolean handled = chainHead.handle(input);
        if (!handled) {
            String msg = "неизвестная команда или неверный формат: " + input;
            logger.logError("FACADE", msg);
            return msg;
        }
        return "обработка завершена";
    }
}