package delivery;

import orders.Order;

public interface Delivery {
    double getCost(final Order order);
    String getEstimatedTime();
    boolean isAvailable(final Order order);
    String getName();

    default Order assignTo(Order order) {
        order.assignDelivery(this);
        return order;
    }
}