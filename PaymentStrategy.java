/**
 * Strategy interface for payments (Behavioral - Strategy)
 */
public interface PaymentStrategy {
    /**
     * Attempt to pay the amount. Return true if payment succeeded.
     * Implementations may simulate or actually integrate payment flows.
     */
    boolean pay(double amount);

    /**
     * Human-readable name for UI
     */
    String getName();
}
