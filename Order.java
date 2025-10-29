import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Simple Order object: immutable snapshot of purchased items and payment method
 * Built via OrderBuilder (Creational - Builder)
 */
public class Order {
    private final List<Product> items;
    private final double total;
    private final PaymentStrategy paymentStrategy;
    private static final DecimalFormat df = new DecimalFormat("#.##");

    Order(List<Product> items, double total, PaymentStrategy paymentStrategy) {
        this.items = new ArrayList<>(items);
        this.total = total;
        this.paymentStrategy = paymentStrategy;
    }

    public List<Product> getItems() {
        return new ArrayList<>(items);
    }

    public double getTotal() {
        return total;
    }

    public PaymentStrategy getPaymentStrategy() {
        return paymentStrategy;
    }

    public String summary() {
        StringBuilder sb = new StringBuilder();
        sb.append("Order Summary:\n");
        for (Product p : items) {
            sb.append("- ").append(p.getDescription()).append("\n");
        }
        sb.append("Total: $").append(df.format(total)).append("\n");
        sb.append("Payment: ").append(paymentStrategy.getName()).append("\n");
        return sb.toString();
    }

    /**
     * Attempt to execute payment via the chosen strategy.
     * Returns true if payment succeeded.
     */
    public boolean checkout() {
        return paymentStrategy.pay(total);
    }
}
