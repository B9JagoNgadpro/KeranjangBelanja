package jagongadpro.keranjang.service;

import jagongadpro.keranjang.model.ShoppingCart;
import jagongadpro.keranjang.dto.KeranjangResponse;
import jagongadpro.keranjang.repository.ShoppingCartRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Service
public class ShoppingCartService {
    @Autowired
    private ShoppingCartRepository shoppingCartRepository;
    private BillingStrategy billingStrategy;

    private final BillingStrategy discountStrategy;
    private final BillingStrategy normalStrategy;


    public ShoppingCartService(ShoppingCartRepository shoppingCartRepository,
                               @Qualifier("discountPricingStrategy") BillingStrategy discountStrategy,
                               @Qualifier("normalPricingStrategy") BillingStrategy normalStrategy) {
        this.shoppingCartRepository = shoppingCartRepository;
        this.discountStrategy = discountStrategy;
        this.normalStrategy = normalStrategy;
        setBillingStrategy();
    }

    private void setBillingStrategy() {
        if (LocalDate.now().getDayOfMonth() == 1) {
            this.billingStrategy = discountStrategy;
        } else {
            this.billingStrategy = normalStrategy;
        }
    }

    public KeranjangResponse addItem(String email, String itemId, int quantity) {
        ShoppingCart cart = shoppingCartRepository.findByEmail(email);
        if (cart == null) {
            throw new IllegalArgumentException("Keranjang dengan email tersebut tidak ditemukan.");
        }

        cart.getItems().put(itemId, cart.getItems().getOrDefault(itemId, 0) + quantity);

        Map<String, Double> itemPrices = getItemPrices(); 
        double totalPrice = billingStrategy.calculateTotal(cart.getItems(), itemPrices);
        cart.setTotalPrice(totalPrice);

        shoppingCartRepository.save(cart);
        return new KeranjangResponse(cart.getEmail(), cart.getItems(), cart.getTotalPrice());
    }

    public KeranjangResponse updateItem(String email, String itemId, int quantity) {
        ShoppingCart cart = shoppingCartRepository.findByEmail(email);
        if (cart == null || !cart.getItems().containsKey(itemId)) {
            throw new IllegalArgumentException("Item tidak ditemukan dalam keranjang.");
        }

        cart.getItems().put(itemId, quantity);

        Map<String, Double> itemPrices = getItemPrices(); 
        double totalPrice = billingStrategy.calculateTotal(cart.getItems(), itemPrices);
        cart.setTotalPrice(totalPrice);

        shoppingCartRepository.save(cart);
        return new KeranjangResponse(cart.getEmail(), cart.getItems(), cart.getTotalPrice());
    }

    public void deleteItem(String email, String itemId) {
        ShoppingCart cart = shoppingCartRepository.findByEmail(email);
        if (cart == null) {
            throw new IllegalArgumentException("Keranjang dengan email tersebut tidak ditemukan.");
        }

        cart.getItems().remove(itemId);

        Map<String, Double> itemPrices = getItemPrices(); 
        double totalPrice = billingStrategy.calculateTotal(cart.getItems(), itemPrices);
        cart.setTotalPrice(totalPrice);

        shoppingCartRepository.save(cart);
    }

    public KeranjangResponse findCartByEmail(String email) {
        ShoppingCart cart = shoppingCartRepository.findByEmail(email);
        if (cart == null) {
            throw new IllegalArgumentException("Keranjang dengan email tersebut tidak ditemukan.");
        }
        return new KeranjangResponse(cart.getEmail(), cart.getItems(), cart.getTotalPrice());
    }

    public void clearCart(String email) {
        ShoppingCart cart = shoppingCartRepository.findByEmail(email);
        if (cart == null) {
            throw new IllegalArgumentException("Keranjang dengan email tersebut tidak ditemukan.");
        }

        cart.getItems().clear();
        cart.setTotalPrice(0);
        shoppingCartRepository.save(cart);
    }

    private Map<String, Double> getItemPrices() {
        // Implementasi untuk mendapatkan harga item
        return new HashMap<>();
    }
}
