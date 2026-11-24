package mediator;

import orders.Order;
import orders.OrderManager;
import orders.OrderStatus;
import products.Product;
import warehouse.Warehouse;

import java.util.*;

public class ShopMediator {
    private final Warehouse warehouse;
    private final OrderManager orderManager;
    private final PaymentSystem paymentSystem;
    private final DeliveryService deliveryService;

    private final Map<Integer, Integer> reservedQtyByProductId = new HashMap<>();

    public ShopMediator(Warehouse warehouse,
                        OrderManager orderManager,
                        PaymentSystem paymentSystem,
                        DeliveryService deliveryService) {
        this.warehouse = warehouse;
        this.orderManager = orderManager;
        this.paymentSystem = paymentSystem;
        this.deliveryService = deliveryService;
    }

    public void onProductAdded(Product product) {
        System.out.printf("mediator: добавлен товар #%d (%s), остаток: %d шт\n",
                product.getId(), product.getName(), product.getQuantity().getValue());
    }

    public void onProductChanged(int productId, String changeInfo) {
        System.out.printf("mediator: изменён товар #%d (%s)\n", productId, changeInfo);
    }

    public void onProductRemoved(int productId) {
        int reserved = reservedQtyByProductId.getOrDefault(productId, 0);
        if (reserved > 0) {
            throw new IllegalStateException(
                    "нельзя удалить товар #" + productId + ": в активных заказах зарезервировано " + reserved + " шт");
        }
        System.out.printf("mediator: товар #%d удалён со склада%n", productId);
    }

    public Order createOrder(String customer, List<Integer> productIds, String deliveryType, String region) {
        if (productIds == null || productIds.isEmpty()) {
            throw new IllegalArgumentException("список товаров для заказа не может быть пустым");
        }

        Map<Integer, Integer> requestedCounts = new LinkedHashMap<>();
        for (Integer id : productIds) {
            requestedCounts.put(id, requestedCounts.getOrDefault(id, 0) + 1);
        }

        for (Map.Entry<Integer, Integer> entry : requestedCounts.entrySet()) {
            int productId = entry.getKey();
            int neededQty = entry.getValue();

            Product stockProduct = warehouse.findById(productId);
            if (stockProduct == null) {
                throw new IllegalArgumentException("товар #" + productId + " не найден на складе");
            }
            if (stockProduct.getQuantity().getValue() < neededQty) {
                throw new IllegalStateException(
                        "недостаточно остатка для товара #" + productId + " (нужно: " + neededQty +
                                ", доступно: " + stockProduct.getQuantity().getValue() + ")");
            }
        }

        List<Product> orderItems = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : requestedCounts.entrySet()) {
            int productId = entry.getKey();
            int neededQty = entry.getValue();

            Product stockProduct = warehouse.findById(productId);
            stockProduct.changeQuantity(-neededQty);
            reservedQtyByProductId.put(productId, reservedQtyByProductId.getOrDefault(productId, 0) + neededQty);

            Product itemCopy = stockProduct.clone();
            int diff = itemCopy.getQuantity().getValue() - neededQty;
            if (diff != 0) {
                itemCopy.changeQuantity(-diff);
            }
            orderItems.add(itemCopy);
        }

        Order order = orderManager.createOrder(customer, orderItems, region);
        deliveryService.scheduleDelivery(order, deliveryType);
        System.out.printf("mediator: заказ #%d сформирован и товары зарезервированы\n", order.getId());
        return order;
    }

    public void confirmPayment(int orderId) {
        Order order = orderManager.findById(orderId);
        if (order == null) {
            throw new IllegalArgumentException("заказ #" + orderId + " не найден");
        }
        if (order.getStatus() != OrderStatus.PENDING) {
            throw new IllegalStateException("оплатить можно только заказ в статусе PENDING");
        }
        if (paymentSystem.processPayment(order)) {
            orderManager.updateStatus(orderId, OrderStatus.PAID);
        }
    }

    public void shipOrder(int orderId) {
        Order order = orderManager.findById(orderId);
        if (order == null) {
            throw new IllegalArgumentException("заказ #" + orderId + " не найден");
        }
        if (order.getStatus() != OrderStatus.PAID) {
            throw new IllegalStateException("отправить можно только оплаченный заказ (PAID)");
        }
        if (!deliveryService.isAvailable(order, order.getDelivery())) {
            throw new IllegalStateException("доставка недоступна для заказа #" + orderId);
        }
        orderManager.updateStatus(orderId, OrderStatus.SHIPPED);
        releaseReservation(order);
        System.out.printf("mediator: заказ #%d отправлен службой '%s'\n",
                orderId, order.getDelivery().getName());
    }

    public void cancelOrder(int orderId) {
        Order order = orderManager.findById(orderId);
        if (order == null) {
            throw new IllegalArgumentException("заказ #" + orderId + " не найден");
        }
        if (order.getStatus() == OrderStatus.SHIPPED || order.getStatus() == OrderStatus.DELIVERED) {
            throw new IllegalStateException("нельзя отменить заказ #" + orderId + " в статусе " + order.getStatus().getTitle());
        }
        if (order.getStatus() == OrderStatus.PAID) {
            paymentSystem.refund(order);
        }
        orderManager.updateStatus(orderId, OrderStatus.CANCELLED);
        restoreStock(order);
        System.out.printf("mediator: заказ #%d отменён, резерв возвращён на склад\n", orderId);
    }

    private void restoreStock(Order order) {
        for (Product item : order.getItems()) {
            int qty = item.getQuantity().getValue();
            int reserved = reservedQtyByProductId.getOrDefault(item.getId(), 0);
            reservedQtyByProductId.put(item.getId(), Math.max(0, reserved - qty));

            Product stockProduct = warehouse.findById(item.getId());
            if (stockProduct != null) {
                stockProduct.changeQuantity(qty);
            }
        }
    }

    private void releaseReservation(Order order) {
        for (Product item : order.getItems()) {
            int qty = item.getQuantity().getValue();
            int reserved = reservedQtyByProductId.getOrDefault(item.getId(), 0);
            reservedQtyByProductId.put(item.getId(), Math.max(0, reserved - qty));
        }
    }

    public String getStatistics() {
        int totalStockUnits = 0;
        int productPositions = 0;
        for (Product p : warehouse) {
            productPositions++;
            totalStockUnits += p.getQuantity().getValue();
        }
        return String.format(
                "статистика: заказов всего=%d, активных=%d, оборот=%.2f руб., клиентов=%d, позиций на складе=%d, единиц товара=%d, проведено оплат=%.2f руб.",
                orderManager.getOrders().size(),
                orderManager.getTotalActiveOrders(),
                orderManager.getTurnover(),
                orderManager.getCustomerCount(),
                productPositions,
                totalStockUnits,
                paymentSystem.checkBalance()
        );
    }
}
