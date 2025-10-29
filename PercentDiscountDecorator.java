import java.text.DecimalFormat;

/**
 * Percent discount decorator (e.g., 10% off)
 */
public class PercentDiscountDecorator extends DiscountDecorator {
    private double percent; // 0-100
    private static final DecimalFormat df = new DecimalFormat("#.##");

    public PercentDiscountDecorator(Product wrapped, double percent) {
        super(wrapped);
        this.percent = percent;
    }

    @Override
    public double getPrice() {
        double p = wrapped.getPrice() * (1.0 - percent / 100.0);
        return Math.max(0, p);
    }

    @Override
    public String getDescription() {
        return wrapped.getName() + " (-" + percent + "%) - $" + df.format(getPrice());
    }
}
