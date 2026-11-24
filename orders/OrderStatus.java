package orders;


public enum OrderStatus {
    PENDING("pending (ожидает оплаты)"),
    PAID("paid (оплачен)"),
    SHIPPED("shipped (отправлен)"),
    DELIVERED("delivered (доставлен)"),
    CANCELLED("cancelled (отменён)");

    private final String title;

    OrderStatus(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}