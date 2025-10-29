/**
 * Observer interface for cart changes (Behavioral - Observer)
 */
public interface CartObserver {
    /**
     * Called when cart content or totals change
     */
    void cartChanged();
}
