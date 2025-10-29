import javax.swing.*;

/**
 * Simulated Card payment strategy
 */
public class CardPayment implements PaymentStrategy {
    private String cardLast4;

    public CardPayment(String cardLast4) {
        this.cardLast4 = cardLast4;
    }

    @Override
    public boolean pay(double amount) {
        // Very simple simulation: ask CVV (just to demonstrate)
        JPanel panel = new JPanel();
        panel.add(new JLabel("Pay $" + String.format("%.2f", amount) + " using card ending " + cardLast4 + ". Enter CVV:"));
        JPasswordField pass = new JPasswordField(4);
        panel.add(pass);
        int res = JOptionPane.showConfirmDialog(null, panel, "Card Payment", JOptionPane.OK_CANCEL_OPTION);
        if (res != JOptionPane.OK_OPTION) return false;
        String cvv = new String(pass.getPassword());
        // very naive validation: cvv length 3
        if (cvv.length() == 3) {
            JOptionPane.showMessageDialog(null, "Card payment authorized.");
            return true;
        } else {
            JOptionPane.showMessageDialog(null, "Invalid CVV. Payment failed.");
            return false;
        }
    }

    @Override
    public String getName() {
        return "Card (**** " + cardLast4 + ")";
    }
}
