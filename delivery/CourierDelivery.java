package delivery;

import orders.Order;

public class CourierDelivery implements Delivery {
    private static final double COST = 500.0;
    private static final double MAX_WEIGHT = 20.0;

    @Override
    public double getCost(final Order order) {
        return COST;
    }

    @Override
    public String getEstimatedTime() {
        return "1-2 дня";
    }

    @Override
    public boolean isAvailable(final Order order) {
        return order.getTotalWeight() <= MAX_WEIGHT && !order.getRegion().equalsIgnoreCase("удаленный регион");
    }

    @Override
    public String getName() {
        return "курьер";
    }
}