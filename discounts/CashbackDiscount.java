package discounts;

import orders.Order;

public class CashbackDiscount implements Discount {
    private final double percent;

    public CashbackDiscount(double percent) {
        if (percent <= 0 || percent > 100) {
            throw new IllegalArgumentException("процент кэшбэка от 1 до 100");
        }
        this.percent = percent;
    }

    @Override
    public void apply(Order order) {
        double cashback = order.getItemsPriceAfterDiscounts() * (percent / 100.0);
        order.addCashback(cashback);
        System.out.printf("discount: начислен кэшбэк %.0f%% (+%.2f бонусов) по заказу #%d%n",
                percent, cashback, order.getId());
    }

    @Override
    public String getDescription() {
        return "кэшбэк " + percent + "%";
    }
}
