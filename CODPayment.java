import javax.swing.*;

/**
 * Cash-on-delivery strategy (always "succeeds")
 */
public class CODPayment implements PaymentStrategy {
    @Override
    public boolean pay(double amount) {
        JOptionPane.showMessageDialog(null, "Order placed. Pay $" + String.format("%.2f", amount) + " on delivery.");
        return true;
    }

    @Override
    public String getName() {
        return "Cash on Delivery";
    }
}
