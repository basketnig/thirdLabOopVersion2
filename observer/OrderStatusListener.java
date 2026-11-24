package observer;

public interface OrderStatusListener {
    void onOrderEvent(final OrderEvent event);
}
