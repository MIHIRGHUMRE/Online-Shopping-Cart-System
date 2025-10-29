import java.util.List;

/**
 * Builder for Order (Creational - Builder pattern)
 */
public class OrderBuilder {
    private Cart cart;
    private PaymentStrategy paymentStrategy;

    public OrderBuilder setCart(Cart cart) {
        this.cart = cart;
        return this;
    }

    public OrderBuilder setPaymentStrategy(PaymentStrategy paymentStrategy) {
        this.paymentStrategy = paymentStrategy;
        return this;
    }

    /**
     * Build the Order. Caller must ensure cart and paymentStrategy are set.
     */
    public Order build() {
        if (cart == null) throw new IllegalStateException("Cart not set");
        if (paymentStrategy == null) throw new IllegalStateException("Payment strategy not set");
        List<Product> items = cart.getItems();
        double total = cart.getTotal();
        return new Order(items, total, paymentStrategy);
    }
}
