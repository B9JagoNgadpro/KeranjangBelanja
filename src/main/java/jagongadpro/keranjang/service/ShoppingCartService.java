package jagongadpro.keranjang.service;

import jagongadpro.keranjang.model.ShoppingCart;
import jagongadpro.keranjang.dto.KeranjangResponse;
import jagongadpro.keranjang.repository.ShoppingCartRepository;
import jagongadpro.keranjang.dto.GameResponse;
import jagongadpro.keranjang.dto.WebResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
public class ShoppingCartService {
    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    private BillingStrategy billingStrategy;

    @Autowired
    private BillingStrategy discountStrategy;

    @Autowired
    private BillingStrategy normalStrategy;

    @Autowired
    private RestTemplate restTemplate;

    public ShoppingCartService(ShoppingCartRepository shoppingCartRepository,
                               @Qualifier("discountPricingStrategy") BillingStrategy discountStrategy,
                               @Qualifier("normalPricingStrategy") BillingStrategy normalStrategy,
                               RestTemplate restTemplate) {
        this.shoppingCartRepository = shoppingCartRepository;
        this.discountStrategy = discountStrategy;
        this.normalStrategy = normalStrategy;
        this.restTemplate = restTemplate;
        setBillingStrategy();
    }

    public void setBillingStrategy() {
        if (LocalDate.now().getDayOfMonth() == 1) {
            this.billingStrategy = discountStrategy;
        } else {
            this.billingStrategy = normalStrategy;
        }
    }

    @Scheduled(cron = "0 0 0 1 * ?")
    @Async
    public CompletableFuture<Void> applyDiscountsToAllCarts() {
        List<ShoppingCart> carts = shoppingCartRepository.findAll();
        for (ShoppingCart cart : carts) {
            Map<String, Double> itemPrices = getItemPrices();
            double totalPrice = discountStrategy.calculateTotal(cart.getItems(), itemPrices);
            cart.setTotalPrice(totalPrice);
            shoppingCartRepository.save(cart);
        }
        return CompletableFuture.completedFuture(null);
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
        cart.setTotalPrice(0.0);

        shoppingCartRepository.save(cart);
    }

    private Map<String, Double> getItemPrices() {
        ResponseEntity<WebResponse<List<GameResponse>>> response = restTemplate.exchange(
                "http://35.240.130.147/api/games/get-all",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<WebResponse<List<GameResponse>>>() {}
        );

        List<GameResponse> games = response.getBody().getData();
        Map<String, Double> itemPrices = new HashMap<>();
        for (GameResponse game : games) {
            itemPrices.put(game.getNama(), game.getHarga().doubleValue());
        }
        return itemPrices;
    }
}
