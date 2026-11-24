package observer;

public interface IObservable {
    void subscribe(OrderStatusListener listener);
    void unsubscribe(OrderStatusListener listener);
    void notifyListeners(final OrderEvent event);
}