package discounts;

import orders.Order;

public interface Discount {
    void apply(Order order);
    String getDescription();
}
