package orders;

import observer.OrderEvent;
import observer.OrderStatusListener;
import products.Product;

import java.util.*;

public class OrderManager implements OrderStatusListener {
    private final List<Order> orders = new ArrayList<>();
    private final Set<String> uniqueCustomers = new HashSet<>();
    private final Map<String, Integer> activeOrdersPerCustomer = new HashMap<>();

    private double turnover = 0.0;
    private final int maxActiveOrdersPerCustomer;
    private final int maxTotalLoad;

    public OrderManager(int maxActiveOrdersPerCustomer, int maxTotalLoad) {
        this.maxActiveOrdersPerCustomer = maxActiveOrdersPerCustomer;
        this.maxTotalLoad = maxTotalLoad;
    }

    public Order createOrder(final String customer, final List<Product> items, final String region) {
        int currentCustomerActive = activeOrdersPerCustomer.getOrDefault(customer, 0);
        if (currentCustomerActive >= maxActiveOrdersPerCustomer) {
            throw new IllegalStateException(String.format(
                    "manager: ошибка клиент '%s' превысил лимит активных заказов (макс. %d)",
                    customer, maxActiveOrdersPerCustomer));
        }

        if (getTotalActiveOrders() >= maxTotalLoad) {
            throw new IllegalStateException("manager: ошибка превышен общий лимит нагрузки системы заказов");
        }

        Order order = new Order(customer, items, region);
        order.subscribe(this);

        orders.add(order);
        uniqueCustomers.add(customer);
        activeOrdersPerCustomer.put(customer, currentCustomerActive + 1);

        System.out.printf("manager: cоздан заказ #%d для клиента '%s'. активных заказов клиента: %d%n",
                order.getId(), customer, currentCustomerActive + 1);
        return order;
    }

    public Order createOrder(final String customer, final List<Product> items) {
        return createOrder(customer, items, "Москва");
    }

    public void updateStatus(int orderId, OrderStatus newStatus) {
        Order order = findById(orderId);
        if (order == null) {
            throw new IllegalArgumentException("заказ с ID " + orderId + " не найден");
        }
        order.setStatus(newStatus);
    }

    @Override
    public void onOrderEvent(final OrderEvent event) {
        Order order = event.getOrder();
        OrderStatus oldStatus = event.getOldStatus();
        OrderStatus newStatus = event.getNewStatus();

        System.out.printf("manager: pаказ #%d сменил статус: %s -> %s%n",
                event.getOrderId(), oldStatus.getTitle(), newStatus.getTitle());

        if (newStatus == OrderStatus.PAID && oldStatus != OrderStatus.PAID) {
            turnover += order.getTotalPrice();
            System.out.printf("manager: заказ #%d оплачен, оборот увеличен на %.2f руб, текущий оборот: %.2f р.%n",
                    order.getId(), order.getTotalPrice(), turnover);
        }

        if ((newStatus == OrderStatus.CANCELLED || newStatus == OrderStatus.DELIVERED)
                && oldStatus != OrderStatus.CANCELLED && oldStatus != OrderStatus.DELIVERED) {
            String customer = order.getCustomer();
            int active = activeOrdersPerCustomer.getOrDefault(customer, 1);
            activeOrdersPerCustomer.put(customer, Math.max(0, active - 1));
            System.out.printf("manager: заказ #%d завершён/отменён, активных заказов у '%s': %d%n",
                    order.getId(), customer, activeOrdersPerCustomer.get(customer));
        }
    }

    public Order findById(int id) {
        for (Order o : orders) {
            if (o.getId() == id) return o;
        }
        return null;
    }

    public int getTotalActiveOrders() {
        int total = 0;
        for (int count : activeOrdersPerCustomer.values()) {
            total += count;
        }
        return total;
    }

    public int getActiveOrdersForCustomer(String customer) {
        return activeOrdersPerCustomer.getOrDefault(customer, 0);
    }

    public double getTurnover() { return turnover; }
    public int getCustomerCount() { return uniqueCustomers.size(); }
    public List<Order> getOrders() { return Collections.unmodifiableList(orders); }
}