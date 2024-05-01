package jagongadpro.keranjang.service;

import jagongadpro.keranjang.model.ShoppingCart;
import jagongadpro.keranjang.dto.KeranjangResponse;
import jagongadpro.keranjang.repository.ShoppingCartRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ShoppingCartService {

    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Autowired
    private BillingStrategy billingStrategy;

    @Autowired
    private PricingService pricingService;

    public KeranjangResponse addItem(String email, int itemId, int quantity) {
        ShoppingCart cart = shoppingCartRepository.findByEmail(email);
        if (cart == null) {
            cart = new ShoppingCart(email);
        }
        cart.getItems().merge(itemId, quantity, Integer::sum);
        double totalPrice = calculateTotalPrice(cart);
        cart.setTotalPrice(totalPrice);
        cart = shoppingCartRepository.save(cart);
        return new KeranjangResponse(cart.getEmail(), cart.getItems(), cart.getTotalPrice());
    }

    public KeranjangResponse updateItem(String email, int itemId, int quantity) {
        ShoppingCart cart = shoppingCartRepository.findByEmail(email);
        if (cart != null && cart.getItems().containsKey(itemId)) {
            cart.getItems().put(itemId, quantity);
            double totalPrice = calculateTotalPrice(cart);
            cart.setTotalPrice(totalPrice);
            shoppingCartRepository.save(cart);
        }
        return new KeranjangResponse(cart.getEmail(), cart.getItems(), cart.getTotalPrice());
    }

    public void deleteItem(String email, int itemId) {
        ShoppingCart cart = shoppingCartRepository.findByEmail(email);
        if (cart != null) {
            cart.getItems().remove(itemId);
            double totalPrice = calculateTotalPrice(cart);
            cart.setTotalPrice(totalPrice);
            shoppingCartRepository.save(cart);
        }
    }

    public KeranjangResponse findCartByEmail(String email) {
        ShoppingCart cart = shoppingCartRepository.findByEmail(email);
        if (cart != null) {
            return new KeranjangResponse(cart.getEmail(), cart.getItems(), cart.getTotalPrice());
        }
        return null;
    }

    private double calculateTotalPrice(ShoppingCart cart) {
        Map<String, Integer> itemMap = new HashMap<>();
        cart.getItems().forEach((id, qty) -> itemMap.put(String.valueOf(id), qty));
        return billingStrategy.calculateTotal(itemMap, pricingService);
    }

    public void clearCart(String email) {
        ShoppingCart cart = shoppingCartRepository.findByEmail(email);
        if (cart != null) {
            cart.getItems().clear();  
            cart.setTotalPrice(0);  
            shoppingCartRepository.save(cart);  
        }
    }
}
