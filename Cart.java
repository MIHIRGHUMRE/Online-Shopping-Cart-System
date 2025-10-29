import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Cart acts as Observable: stores products and notifies observers on change
 */
public class Cart {
    private final List<Product> items = new ArrayList<>();
    private final List<CartObserver> observers = new ArrayList<>();

    public synchronized void addObserver(CartObserver o) {
        if (!observers.contains(o)) observers.add(o);
    }

    public synchronized void removeObserver(CartObserver o) {
        observers.remove(o);
    }

    private synchronized void notifyObservers() {
        for (CartObserver o : observers) {
            o.cartChanged();
        }
    }

    public synchronized void addProduct(Product p) {
        items.add(p);
        notifyObservers();
    }

    public synchronized void removeProduct(int index) {
        if (index >= 0 && index < items.size()) {
            items.remove(index);
            notifyObservers();
        }
    }

    public synchronized List<Product> getItems() {
        return Collections.unmodifiableList(new ArrayList<>(items));
    }

    public synchronized double getTotal() {
        double sum = 0.0;
        for (Product p : items) sum += p.getPrice();
        return sum;
    }

    public synchronized void clear() {
        items.clear();
        notifyObservers();
    }

    public synchronized int size() {
        return items.size();
    }
}
