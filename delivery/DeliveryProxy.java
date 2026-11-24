package delivery;

import orders.Order;
import java.util.HashMap;
import java.util.Map;

public class DeliveryProxy implements Delivery {
    private Delivery realDelivery;
    private final Delivery fallbackDelivery;
    private final Map<String, Double> costCache = new HashMap<>();

    public DeliveryProxy(Delivery realDelivery) {
        this.realDelivery = realDelivery;
        this.fallbackDelivery = new PickupDelivery();
    }

    @Override
    public boolean isAvailable(final Order order) {
        System.out.printf("delivery proxy: проверка доступности '%s' для заказа #%d вес: %.2f кг, регион: %s\n",
                realDelivery.getName(), order.getId(), order.getTotalWeight(), order.getRegion());

        boolean available = realDelivery.isAvailable(order);
        if (!available) {
            System.out.printf("delivery proxy: '%s' недоступна, автоматическое переключение на '%s'.\n",
                    realDelivery.getName(), fallbackDelivery.getName());
            this.realDelivery = fallbackDelivery;
            return this.realDelivery.isAvailable(order);
        }
        return true;
    }

    @Override
    public double getCost(final Order order) {
        if (!realDelivery.isAvailable(order)) {
            isAvailable(order);
        }

        String cacheKey = order.getId() + "_" + String.format("%.2f", order.getTotalWeight()) + "_" + realDelivery.getName();
        if (costCache.containsKey(cacheKey)) {
            System.out.printf("delivery proxy: Возврат кешированной стоимости доставки для заказа #%d: %.2f руб.\n",
                    order.getId(), costCache.get(cacheKey));
            return costCache.get(cacheKey);
        }

        double cost = realDelivery.getCost(order);
        costCache.put(cacheKey, cost);
        System.out.printf("delivery proxy: расчёт и кеширование стоимости '%s' для заказа #%d: %.2f руб.\n",
                realDelivery.getName(), order.getId(), cost);
        return cost;
    }

    @Override
    public String getEstimatedTime() {
        return realDelivery.getEstimatedTime();
    }

    @Override
    public String getName() {
        return realDelivery.getName();
    }
}
