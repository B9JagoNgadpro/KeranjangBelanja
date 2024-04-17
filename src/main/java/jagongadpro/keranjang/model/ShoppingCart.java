package jagongadpro.keranjang.model;

import java.util.HashMap;
import java.util.Map;

public class ShoppingCart {
    private static ShoppingCart instance;
    // Asumsi untuk model game nantinya, karena model game belum dibuat 
    private Map<String, Integer> items;
    private Map<String, Double> prices;

    private ShoppingCart() {
        items = new HashMap<>();
        prices = new HashMap<>();
        prices.put("game001", 50000.0);
        prices.put("game002", 30000.0);
    }

    // disini saya menggunakan singleton pattern 
    public static ShoppingCart getInstance() {
        if (instance == null) {
            instance = new ShoppingCart();
        }
        return instance;
    }

    public void addItem(String id, int quantity) {
        if (items.containsKey(id)) {
            items.put(id, items.get(id) + quantity);
        } else {
            items.put(id, quantity);
        }
    }

    public void removeItem(String id) {
        items.remove(id);
    }

    public void updateItem(String id, int quantity) {
        if (items.containsKey(id)) {
            items.put(id, quantity);
        }
    }

    public Map<String, Integer> getItems() {
        return items;
    }

    public double calculateTotalPrice() {
        double total = 0.0;
        for (Map.Entry<String, Integer> entry : items.entrySet()) {
            Double price = prices.get(entry.getKey());
            if (price != null) {
                total += price * entry.getValue();
            }
        }
        return total;
    }

    // Method untuk reset singleton instance
    public static void resetInstance() {
        instance = null;
    }
}
