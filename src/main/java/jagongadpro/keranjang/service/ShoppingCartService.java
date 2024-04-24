package jagongadpro.keranjang.service;

import jagongadpro.keranjang.model.ShoppingCart;
import jagongadpro.keranjang.repository.ShoppingCartRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ShoppingCartService {

    private final ShoppingCartRepository shoppingCartRepository;

    public ShoppingCartService() {
        this.shoppingCartRepository = ShoppingCartRepository.getInstance();
    }

    public ShoppingCart addItem(int itemId, int quantity) {
        return shoppingCartRepository.addItem(itemId, quantity);
    }

    public List<ShoppingCart> findAllItems() {
        List<ShoppingCart> allItems = new ArrayList<>(shoppingCartRepository.getCartItems().values());
        return allItems;
    }

    public ShoppingCart updateItem(int itemId, int quantity) {
        return shoppingCartRepository.updateItem(itemId, quantity);
    }

    public void deleteItem(int itemId) {
        shoppingCartRepository.removeItem(itemId);
    }

    public ShoppingCart findItemById(int itemId) {
        return shoppingCartRepository.getItem(itemId);
    }
}
