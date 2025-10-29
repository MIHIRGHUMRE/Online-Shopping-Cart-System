import javax.swing.*;

/**
 * Simulated UPI payment strategy
 */
public class UPIPayment implements PaymentStrategy {
    private String upiId;

    public UPIPayment(String upiId) {
        this.upiId = upiId;
    }

    @Override
    public boolean pay(double amount) {
        // For demo: show a simple dialog to simulate payment confirmation
        int res = JOptionPane.showConfirmDialog(null,
                "Pay $" + String.format("%.2f", amount) + " via UPI (" + upiId + ")?",
                "Confirm UPI Payment", JOptionPane.YES_NO_OPTION);
        return res == JOptionPane.YES_OPTION;
    }

    @Override
    public String getName() {
        return "UPI (" + upiId + ")";
    }
}
