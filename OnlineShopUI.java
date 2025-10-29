import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * Simple Swing UI for the shopping app.
 * Implements CartObserver to receive updates (Behavioral - Observer)
 */
public class OnlineShopUI extends JFrame implements CartObserver {
    private final DefaultListModel<Product> catalogModel = new DefaultListModel<>();
    private final JList<Product> catalogList = new JList<>(catalogModel);

    private final DefaultListModel<Product> cartModel = new DefaultListModel<>();
    private final JList<Product> cartList = new JList<>(cartModel);

    private final Cart cart;
    private final JLabel totalLabel = new JLabel("Total: $0.00");

    private final JComboBox<String> discountCombo = new JComboBox<>();
    private final JComboBox<String> paymentCombo = new JComboBox<>();

    public OnlineShopUI(Cart cart) {
        this.cart = cart;
        setTitle("Simple Online Shop");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 450);
        setLocationRelativeTo(null);
        initUI();
        cart.addObserver(this);
    }

    private void initUI() {
        JPanel root = new JPanel(new BorderLayout(10,10));
        root.setBorder(new EmptyBorder(10,10,10,10));
        setContentPane(root);

        // Left: Catalog
        JPanel left = new JPanel(new BorderLayout(6,6));
        left.setPreferredSize(new Dimension(320, 0));
        left.add(new JLabel("Catalog"), BorderLayout.NORTH);
        catalogList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        left.add(new JScrollPane(catalogList), BorderLayout.CENTER);

        JPanel leftBtns = new JPanel(new GridLayout(1,2,6,6));
        JButton addBtn = new JButton("Add to Cart");
        JButton detailsBtn = new JButton("Details");
        leftBtns.add(addBtn);
        leftBtns.add(detailsBtn);
        left.add(leftBtns, BorderLayout.SOUTH);

        // Middle: Controls
        JPanel middle = new JPanel(new BorderLayout(6,6));
        middle.setPreferredSize(new Dimension(160,0));
        JPanel discountPanel = new JPanel(new GridLayout(3,1,6,6));
        discountPanel.setBorder(BorderFactory.createTitledBorder("Discount"));
        discountCombo.addItem("No Discount");
        discountCombo.addItem("10% Off");
        discountCombo.addItem("$5 Off");
        discountPanel.add(discountCombo);
        JButton applyDiscountBtn = new JButton("Apply Discount to Selected in Catalog");
        discountPanel.add(applyDiscountBtn);
        JButton clearDiscountBtn = new JButton("Clear Discounts (catalog)");
        discountPanel.add(clearDiscountBtn);
        middle.add(discountPanel, BorderLayout.NORTH);

        JPanel paymentPanel = new JPanel(new GridLayout(4,1,6,6));
        paymentPanel.setBorder(BorderFactory.createTitledBorder("Payment"));
        paymentCombo.addItem("UPI (demo@upi)");
        paymentCombo.addItem("Card (4242)");
        paymentCombo.addItem("Cash on Delivery");
        JButton checkoutBtn = new JButton("Checkout");
        paymentPanel.add(paymentCombo);
        paymentPanel.add(new JLabel()); // spacer
        paymentPanel.add(new JLabel()); // spacer
        paymentPanel.add(checkoutBtn);
        middle.add(paymentPanel, BorderLayout.SOUTH);

        // Right: Cart
        JPanel right = new JPanel(new BorderLayout(6,6));
        right.add(new JLabel("Your Cart"), BorderLayout.NORTH);
        cartList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        right.add(new JScrollPane(cartList), BorderLayout.CENTER);

        JPanel cartBtns = new JPanel(new GridLayout(2,2,6,6));
        JButton removeBtn = new JButton("Remove");
        JButton clearBtn = new JButton("Clear Cart");
        cartBtns.add(removeBtn);
        cartBtns.add(clearBtn);
        cartBtns.add(totalLabel);
        cartBtns.add(new JLabel());
        right.add(cartBtns, BorderLayout.SOUTH);

        root.add(left, BorderLayout.WEST);
        root.add(middle, BorderLayout.CENTER);
        root.add(right, BorderLayout.EAST);

        // Fill catalog
        populateCatalog();

        // Event handlers
        addBtn.addActionListener(e -> {
            Product sel = catalogList.getSelectedValue();
            if (sel == null) {
                JOptionPane.showMessageDialog(this, "Select a product first.");
                return;
            }
            // When adding to cart, apply the currently visible decorator from catalog object
            // We will add the catalog item instance itself (catalog stores Product references)
            cart.addProduct(sel);
            updateCartModel();
        });

        detailsBtn.addActionListener(e -> {
            Product sel = catalogList.getSelectedValue();
            if (sel == null) {
                JOptionPane.showMessageDialog(this, "Select a product first.");
                return;
            }
            JOptionPane.showMessageDialog(this, sel.getDescription(), "Product Details", JOptionPane.INFORMATION_MESSAGE);
        });

        applyDiscountBtn.addActionListener(e -> {
            int idx = catalogList.getSelectedIndex();
            if (idx < 0) { JOptionPane.showMessageDialog(this, "Select a catalog item to discount."); return; }
            Product orig = catalogModel.getElementAt(idx);
            String choice = (String) discountCombo.getSelectedItem();
            Product decorated = orig;
            if ("10% Off".equals(choice)) {
                decorated = new PercentDiscountDecorator(orig, 10.0);
            } else if ("$5 Off".equals(choice)) {
                decorated = new FlatDiscountDecorator(orig, 5.0);
            }
            // replace catalog item with decorated version (so future adds use it)
            catalogModel.setElementAt(decorated, idx);
            JOptionPane.showMessageDialog(this, "Discount applied to catalog item.");
        });

        clearDiscountBtn.addActionListener(e -> {
            // Reset catalog to base items (repopulate)
            populateCatalog();
            JOptionPane.showMessageDialog(this, "Catalog discounts cleared.");
        });

        removeBtn.addActionListener(e -> {
            int sel = cartList.getSelectedIndex();
            if (sel >= 0) {
                cart.removeProduct(sel);
                updateCartModel();
            }
        });

        clearBtn.addActionListener(e -> {
            cart.clear();
            updateCartModel();
        });

        checkoutBtn.addActionListener(e -> {
            if (cart.size() == 0) {
                JOptionPane.showMessageDialog(this, "Cart is empty.");
                return;
            }
            // choose payment
            String p = (String) paymentCombo.getSelectedItem();
            PaymentStrategy strategy;
            if (p.startsWith("UPI")) strategy = new UPIPayment("demo@upi");
            else if (p.startsWith("Card")) strategy = new CardPayment("4242");
            else strategy = new CODPayment();

            // Build order
            OrderBuilder builder = new OrderBuilder();
            builder.setCart(cart).setPaymentStrategy(strategy);
            Order order = builder.build();

            // show summary
            int confirmed = JOptionPane.showConfirmDialog(this, order.summary() + "\nProceed to pay?", "Confirm Order", JOptionPane.YES_NO_OPTION);
            if (confirmed != JOptionPane.YES_OPTION) return;

            // Attempt checkout
            boolean ok = order.checkout();
            if (ok) {
                JOptionPane.showMessageDialog(this, "Payment successful. Order placed!");
                cart.clear();
                updateCartModel();
            } else {
                JOptionPane.showMessageDialog(this, "Payment failed or cancelled.");
            }
        });

        // initial update
        updateCartModel();
    }

    private void populateCatalog() {
        catalogModel.clear();
        catalogModel.addElement(new Product("Notebook", 4.99));
        catalogModel.addElement(new Product("Water Bottle", 12.50));
        catalogModel.addElement(new Product("Bluetooth Earbuds", 29.99));
        catalogModel.addElement(new Product("T-Shirt", 19.00));
        catalogModel.addElement(new Product("Coffee Mug", 8.75));
    }

    private void updateCartModel() {
        // Update cart list (observer triggers this method also via cartChanged)
        SwingUtilities.invokeLater(() -> {
            cartModel.clear();
            for (Product p : cart.getItems()) {
                cartModel.addElement(p);
            }
            totalLabel.setText(String.format("Total: $%.2f", cart.getTotal()));
        });
    }

    @Override
    public void cartChanged() {
        updateCartModel();
    }
}
