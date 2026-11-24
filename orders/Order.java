package orders;

import delivery.Delivery;
import discounts.Discount;
import observer.IObservable;
import observer.OrderEvent;
import observer.OrderStatusListener;
import products.Product;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Order implements IObservable, Cloneable {
    private static int nextOrderId = 1;

    private final int id;
    private final String customer;
    private final String region;
    private List<Product> items;
    private OrderStatus status;
    private Delivery delivery;
    private List<Discount> discounts;

    private double itemsPriceAfterDiscounts;
    private double cashbackEarned = 0.0;
    private boolean freeDelivery = false;

    private final List<OrderStatusListener> listeners = new ArrayList<>();

    public Order(String customer, List<Product> initialItems, String region) {
        this.id = nextOrderId++;
        this.customer = customer;
        this.region = region;
        this.items = new ArrayList<>();
        if (initialItems != null) {
            for (Product p : initialItems) {
                this.items.add(p.clone());
            }
        }
        this.status = OrderStatus.PENDING;
        this.discounts = new ArrayList<>();
        recalculateBaseItemsPrice();
    }

    public Order(final Order other) {
        this.id = other.id;
        this.customer = other.customer;
        this.region = other.region;
        this.status = other.status;
        this.delivery = other.delivery;
        this.itemsPriceAfterDiscounts = other.itemsPriceAfterDiscounts;
        this.cashbackEarned = other.cashbackEarned;
        this.freeDelivery = other.freeDelivery;
        this.items = new ArrayList<>();
        for (Product p : other.items) {
            this.items.add(p.clone());
        }
        this.discounts = new ArrayList<>(other.discounts);
    }

    private void recalculateBaseItemsPrice() {
        double sum = 0;
        for (Product p : items) {
            sum += p.getTotalPrice();
        }
        this.itemsPriceAfterDiscounts = sum;
    }

    public void addItem(final Product product) {
        Product copy = product.clone();
        this.items.add(copy);
        this.itemsPriceAfterDiscounts += copy.getTotalPrice();
    }

    public void addGiftItem(final Product gift) {
        this.items.add(gift);
    }

    public void removeItem(int productId) {
        items.removeIf(p -> p.getId() == productId);
        recalculateBaseItemsPrice();
    }

    public double getTotalWeight() {
        double weight = 0;
        for (Product p : items) {
            weight += p.getTotalWeight();
        }
        return weight;
    }

    public double getDeliveryCost() {
        if (freeDelivery || delivery == null) return 0.0;
        return delivery.getCost(this);
    }

    public double getTotalPrice() {
        return itemsPriceAfterDiscounts + getDeliveryCost();
    }

    public Order applyDiscount(final Discount discount) {
        this.discounts.add(discount);
        discount.apply(this);
        return this;
    }

    public Order assignDelivery(final Delivery delivery) {
        if (delivery.isAvailable(this)) {
            this.delivery = delivery;
            System.out.printf("order: cпособ доставки '%s' назначен заказу #%d%n", delivery.getName(), id);
        } else {
            throw new IllegalStateException("доставка '" + delivery.getName() + "' недоступна для заказа #" + id);
        }
        return this;
    }

    public void setDelivery(final Delivery delivery) {
        assignDelivery(delivery);
    }

    public void setStatus(OrderStatus newStatus) {
        if (this.status == newStatus) return;
        OrderStatus oldStatus = this.status;
        this.status = newStatus;
        notifyListeners(new OrderEvent(this, oldStatus, newStatus));
    }

    public void reduceItemsPrice(double amount) {
        this.itemsPriceAfterDiscounts = Math.max(0, this.itemsPriceAfterDiscounts - amount);
    }

    public void addCashback(double amount) {
        this.cashbackEarned += amount;
    }

    public void setFreeDelivery(boolean freeDelivery) {
        this.freeDelivery = freeDelivery;
    }

    public int getId() { return id; }
    public String getCustomer() { return customer; }
    public String getRegion() { return region; }
    public OrderStatus getStatus() { return status; }
    public Delivery getDelivery() { return delivery; }
    public double getItemsPriceAfterDiscounts() { return itemsPriceAfterDiscounts; }
    public double getCashbackEarned() { return cashbackEarned; }

    public List<Product> getItems() {
        return Collections.unmodifiableList(items);
    }

    @Override
    public void subscribe(OrderStatusListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    @Override
    public void unsubscribe(OrderStatusListener listener) {
        listeners.remove(listener);
    }

    @Override
    public void notifyListeners(final OrderEvent event) {
        for (OrderStatusListener listener : listeners) {
            listener.onOrderEvent(event);
        }
    }

    @Override
    public Order clone() {
        return new Order(this);
    }

    @Override
    public String toString() {
        String deliveryInfo = (delivery != null)
                ? String.format("%s (%.2f руб., срок: %s)", delivery.getName(), getDeliveryCost(), delivery.getEstimatedTime())
                : "не выбрана";
        return String.format(
                "заказ #%d, клиент: %s (%s), статус: %s, позиций: %d, вес: %.2f кг, доставка: %s, итого: %.2f руб. (кэшбэк: %.2f)",
                id, customer, region, status.getTitle(), items.size(), getTotalWeight(), deliveryInfo, getTotalPrice(), cashbackEarned
        );
    }
}
