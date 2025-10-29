import javax.swing.*;

/**
 * Main launcher
 */
public class OnlineShoppingApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Cart cart = new Cart();
            OnlineShopUI ui = new OnlineShopUI(cart);
            ui.setVisible(true);
        });
    }
}
