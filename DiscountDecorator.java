/**
 * Abstract Decorator for Product - Structural Pattern (Decorator)
 */
public abstract class DiscountDecorator extends Product {
    protected Product wrapped;

    public DiscountDecorator(Product wrapped) {
        // name and basePrice here aren't used directly; delegate to wrapped
        super(wrapped.getName(), wrapped.getPrice());
        this.wrapped = wrapped;
    }

    @Override
    public double getPrice() {
        return wrapped.getPrice();
    }

    @Override
    public String getDescription() {
        return wrapped.getDescription();
    }

    public Product getWrapped() {
        return wrapped;
    }
}
