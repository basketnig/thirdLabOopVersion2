package discounts;

import orders.Order;
import products.Product;
import attributes.Price;

public class GiftDiscount implements Discount {
    private final Product giftTemplate;

    public GiftDiscount(Product giftTemplate) {
        this.giftTemplate = giftTemplate;
    }

    @Override
    public void apply(Order order) {
        Product gift = giftTemplate.clone();
        gift.setPrice(new Price(0));
        order.addGiftItem(gift);
        System.out.printf("discount: в заказ #%d добавлен бесплатный подарок: %s%n",
                order.getId(), gift.getName());
    }

    @Override
    public String getDescription() {
        return "подарок к заказу (" + giftTemplate.getName() + ")";
    }
}
