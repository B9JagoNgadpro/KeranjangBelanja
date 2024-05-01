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

    private final ShoppingCartRepository shoppingCartRepository;
    private final BillingStrategy billingStrategy;
    private final PricingService pricingService;

    @Autowired
    public ShoppingCartService(ShoppingCartRepository shoppingCartRepository,
                               BillingStrategy billingStrategy,
                               PricingService pricingService) {
        this.shoppingCartRepository = shoppingCartRepository;
        this.billingStrategy = billingStrategy;
        this.pricingService = pricingService;
    }

    public KeranjangResponse addItem(String email, int itemId, int quantity) {
        ShoppingCart cart = shoppingCartRepository.findByEmail(email);
        if (cart == null) {
            cart = new ShoppingCart(email);
        }
        cart.getItems().merge(itemId, quantity, Integer::sum);

        // Calculate total price using billing strategy
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

    private double calculateTotalPrice(ShoppingCart cart) {
        Map<String, Integer> itemMap = new HashMap<>();
        cart.getItems().forEach((id, qty) -> itemMap.put(String.valueOf(id), qty));
        return billingStrategy.calculateTotal(itemMap, pricingService);
    }
}
