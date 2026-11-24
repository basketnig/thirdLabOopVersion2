package mediator;

import delivery.*;
import orders.Order;

public class DeliveryService {
    public Delivery createDelivery(String deliveryType) {
        if (deliveryType == null) {
            return new DeliveryProxy(new PickupDelivery());
        }
        return switch (deliveryType.toLowerCase()) {
            case "courier", "курьер" -> new DeliveryProxy(new CourierDelivery());
            case "post", "почта" -> new DeliveryProxy(new PostDelivery());
            case "pickup", "самовывоз" -> new DeliveryProxy(new PickupDelivery());
            default -> throw new IllegalArgumentException("неизвестный тип доставки: " + deliveryType);
        };
    }

    public boolean isAvailable(Order order, Delivery delivery) {
        return order != null && delivery != null && delivery.isAvailable(order);
    }

    public double calculateCost(Order order, Delivery delivery) {
        if (order == null || delivery == null) {
            return 0.0;
        }
        return delivery.getCost(order);
    }

    public void scheduleDelivery(Order order, String deliveryType) {
        Delivery delivery = createDelivery(deliveryType);
        delivery.assignTo(order);
        System.out.printf("delivery: доставка '%s' назначена заказу #%d (стоимость: %.2f руб., срок: %s)\n",
                order.getDelivery().getName(),
                order.getId(),
                order.getDeliveryCost(),
                order.getDelivery().getEstimatedTime());
    }
}