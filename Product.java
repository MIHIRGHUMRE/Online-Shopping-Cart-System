import java.text.DecimalFormat;

/**
 * Base Product class
 */
public class Product {
    protected String name;
    protected double basePrice;

    private static final DecimalFormat df = new DecimalFormat("#.##");

    public Product(String name, double basePrice) {
        this.name = name;
        this.basePrice = basePrice;
    }

    public String getName() {
        return name;
    }

    /**
     * Returns effective price (may be overridden by decorator)
     */
    public double getPrice() {
        return basePrice;
    }

    /**
     * Description for display (name + price)
     */
    public String getDescription() {
        return name + " - $" + df.format(getPrice());
    }

    @Override
    public String toString() {
        return getDescription();
    }
}
