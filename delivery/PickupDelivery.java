package delivery;

import orders.Order;

public class PickupDelivery implements Delivery {
    @Override
    public double getCost(final Order order) {
        return 0.0;
    }

    @Override
    public String getEstimatedTime() {
        return "1-3 дня";
    }

    @Override
    public boolean isAvailable(final Order order) {
        return true;
    }

    @Override
    public String getName() {
        return "самовывоз";
    }
}
