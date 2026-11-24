package mediator;

import orders.Order;

public class PaymentSystem {
    private double processedAmount = 0.0;

    public boolean processPayment(Order order) {
        if (order == null) {
            return false;
        }
        double amount = order.getTotalPrice();
        processedAmount += amount;
        System.out.printf("payment: оплата заказа #%d на сумму %.2f руб. подтверждена\n", order.getId(), amount);
        return true;
    }

    public void refund(Order order) {
        if (order == null) {
            return;
        }
        double amount = order.getTotalPrice();
        processedAmount = Math.max(0.0, processedAmount - amount);
        System.out.printf("payment: возврат средств по заказу #%d на сумму %.2f руб.\n", order.getId(), amount);
    }

    public double checkBalance() {
        return processedAmount;
    }
}
