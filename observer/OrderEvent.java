package observer;


import orders.Order;
import orders.OrderStatus;
import java.time.LocalDateTime;

public class OrderEvent {
    private final int orderId;
    private final Order order;
    private final OrderStatus oldStatus;
    private final OrderStatus newStatus;
    private final LocalDateTime timestamp;

    public OrderEvent(Order order, OrderStatus oldStatus, OrderStatus newStatus) {
        this.orderId = order.getId();
        this.order = order;
        this.oldStatus = oldStatus;
        this.newStatus = newStatus;
        this.timestamp = LocalDateTime.now();
    }

    public int getOrderId() { return orderId; }
    public Order getOrder() { return order; }
    public OrderStatus getOldStatus() { return oldStatus; }
    public OrderStatus getNewStatus() { return newStatus; }
    public LocalDateTime getTimestamp() { return timestamp; }
}