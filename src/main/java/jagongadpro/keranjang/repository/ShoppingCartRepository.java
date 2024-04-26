package jagongadpro.keranjang.repository;

import jagongadpro.keranjang.model.ShoppingCart;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class ShoppingCartRepository {
    private static ShoppingCartRepository instance;
    private Map<Integer, ShoppingCart> cartItems = new HashMap<>();

    // Singleton pattern
    //private ShoppingCartRepository() {}

    // Singleton global access
    public static synchronized ShoppingCartRepository getInstance() {
        if (instance == null) {
            instance = new ShoppingCartRepository();
        }
        return instance;
    }

    public ShoppingCart addItem(int itemId, int quantity) {
        if (cartItems.containsKey(itemId)) {
            ShoppingCart existingItem = cartItems.get(itemId);
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
            return existingItem;
        } else {
            ShoppingCart newItem = new ShoppingCart(itemId, quantity);
            cartItems.put(itemId, newItem);
            return newItem;
        }
    }

    public ShoppingCart getItem(int itemId) {
        return cartItems.get(itemId);
    }

    public ShoppingCart updateItem(int itemId, int quantity) {
        if (cartItems.containsKey(itemId)) {
            ShoppingCart item = cartItems.get(itemId);
            item.setQuantity(quantity);
            return item;
        }
        return null;
    }

    public void removeItem(int itemId) {
        cartItems.remove(itemId);
    }

    public Map<Integer, ShoppingCart> getAllItems() {
        return new HashMap<>(cartItems);
    }

    public static synchronized void resetInstance() {
        instance = null;
    }
}
