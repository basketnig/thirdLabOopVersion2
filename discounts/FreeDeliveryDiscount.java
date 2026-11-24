package discounts;

import orders.Order;

public class FreeDeliveryDiscount implements Discount {
    @Override
    public void apply(Order order) {
        order.setFreeDelivery(true);
        System.out.printf("discount: применена акция 'Бесплатная доставка' к заказу #%d%n", order.getId());
    }

    @Override
    public String getDescription() {
        return "бесплатная доставка";
    }
}
