package discounts;

import orders.Order;

public class PercentDiscount implements Discount {
    private final double percent;

    public PercentDiscount(double percent) {
        if (percent <= 0 || percent > 100) {
            throw new IllegalArgumentException("процент скидки от 1 до 100");
        }
        this.percent = percent;
    }

    @Override
    public void apply(Order order) {
        double discountAmount = order.getItemsPriceAfterDiscounts() * (percent / 100.0);
        order.reduceItemsPrice(discountAmount);
        System.out.printf("discount: применена скидка %.0f%% (-%.2f руб.) к заказу #%d\n",
                percent, discountAmount, order.getId());
    }

    @Override
    public String getDescription() {
        return "скидка " + percent + "%";
    }
}
