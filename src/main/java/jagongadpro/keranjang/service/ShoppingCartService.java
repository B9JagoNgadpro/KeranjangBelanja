package jagongadpro.keranjang.service;

import jagongadpro.keranjang.model.ShoppingCart;
import jagongadpro.keranjang.repository.ShoppingCartRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ShoppingCartService {

    private final ShoppingCartRepository shoppingCartRepository;

    public ShoppingCartService() {
        this.shoppingCartRepository = ShoppingCartRepository.getInstance();
    }

    // Konstruktor tambahan untuk testing
    public ShoppingCartService(ShoppingCartRepository repository) {
        this.shoppingCartRepository = repository;
    }


    public ShoppingCart addItem(int itemId, int quantity) {
        return shoppingCartRepository.addItem(itemId, quantity);
    }

    public List<ShoppingCart> findAllItems() {
        Map<Integer, ShoppingCart> items = shoppingCartRepository.getAllItems();
        return new ArrayList<>(items.values());
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
