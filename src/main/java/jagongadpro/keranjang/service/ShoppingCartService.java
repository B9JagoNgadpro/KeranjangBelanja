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


    public ShoppingCartService(ShoppingCartRepository shoppingCartRepository,
                               @Qualifier("discountPricingStrategy") BillingStrategy discountStrategy,
                               @Qualifier("normalPricingStrategy") BillingStrategy normalStrategy) {
        this.shoppingCartRepository = shoppingCartRepository;
        if (LocalDate.now().getDayOfMonth() == 1) {
            this.billingStrategy = discountStrategy;
        } else {
            this.billingStrategy = normalStrategy;
        }
    }

    public KeranjangResponse addItem(String email, int itemId, int quantity) {
        ShoppingCart cart = shoppingCartRepository.findByEmail(email);
        if (cart == null) {
            cart = new ShoppingCart(email);
        }
        cart.getItems().merge(itemId, quantity, Integer::sum);

        Map<String, Double> itemPrices = new HashMap<>();
        Map<String, Integer> itemQuantities = new HashMap<>();
        cart.getItems().forEach((key, value) -> itemQuantities.put(String.valueOf(key), value));

        double totalPrice = billingStrategy.calculateTotal(itemQuantities, itemPrices);
        cart.setTotalPrice(totalPrice);

        cart = shoppingCartRepository.save(cart);
        return new KeranjangResponse(cart.getEmail(), cart.getItems(), cart.getTotalPrice());
    }

    public KeranjangResponse updateItem(String email, int itemId, int quantity) {
        ShoppingCart cart = shoppingCartRepository.findByEmail(email);
        if (cart != null && cart.getItems().containsKey(itemId)) {
            cart.getItems().put(itemId, quantity);

            Map<String, Double> itemPrices = new HashMap<>();
            Map<String, Integer> itemQuantities = new HashMap<>();
            cart.getItems().forEach((key, value) -> itemQuantities.put(String.valueOf(key), value));

            double totalPrice = billingStrategy.calculateTotal(itemQuantities, itemPrices);
            cart.setTotalPrice(totalPrice);

            shoppingCartRepository.save(cart);
        }
        return new KeranjangResponse(cart.getEmail(), cart.getItems(), cart.getTotalPrice());
    }

    public void deleteItem(String email, int itemId) {
        ShoppingCart cart = shoppingCartRepository.findByEmail(email);
        if (cart != null) {
            cart.getItems().remove(itemId);

            Map<String, Double> itemPrices = new HashMap<>();
            Map<String, Integer> itemQuantities = new HashMap<>();
            cart.getItems().forEach((key, value) -> itemQuantities.put(String.valueOf(key), value));

            double totalPrice = billingStrategy.calculateTotal(itemQuantities, itemPrices);
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

    public void clearCart(String email) {
        ShoppingCart cart = shoppingCartRepository.findByEmail(email);
        if (cart != null) {
            cart.getItems().clear();
            cart.setTotalPrice(0);
            shoppingCartRepository.save(cart);
        }
    }
}
