import java.text.DecimalFormat;

/**
 * Flat discount decorator (e.g., $5 off)
 */
public class FlatDiscountDecorator extends DiscountDecorator {
    private double amount;
    private static final DecimalFormat df = new DecimalFormat("#.##");

    public FlatDiscountDecorator(Product wrapped, double amount) {
        super(wrapped);
        this.amount = amount;
    }

    @Override
    public double getPrice() {
        double p = wrapped.getPrice() - amount;
        return Math.max(0, p);
    }

    @Override
    public String getDescription() {
        return wrapped.getName() + " (-$" + df.format(amount) + ") - $" + df.format(getPrice());
    }
}
