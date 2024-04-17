package jagongadpro.keranjang.model;

import java.util.HashMap;
import java.util.Map;

public class ShoppingCart {
    private static ShoppingCart instance;
    private Map<String, Integer> items;

    private ShoppingCart() {
        items = new HashMap<>();
    }

    public static ShoppingCart getInstance() {
        if (instance == null) {
            instance = new ShoppingCart();
        }
        return instance;
    }

    public void addItem(String id, int quantity) {}

    public void removeItem(String id) {}

    public void updateItem(String id, int quantity) {}

    public Map<String, Integer> getItems() {
        return items;
    }

    public double calculateTotalPrice() {
        return 0.0;
    }

    // This method is used for testing to reset the singleton instance
    public static void resetInstance() {
        instance = null;
    }
}
