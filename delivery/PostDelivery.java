package delivery;

import orders.Order;

public class PostDelivery implements Delivery {
    private static final double COST = 200.0;
    private static final double MAX_WEIGHT = 10.0;

    @Override
    public double getCost(final Order order) {
        return COST;
    }

    @Override
    public String getEstimatedTime() {
        return "5-10 дней";
    }

    @Override
    public boolean isAvailable(final Order order) {
        return order.getTotalWeight() <= MAX_WEIGHT;
    }

    @Override
    public String getName() {
        return "почта";
    }
}
