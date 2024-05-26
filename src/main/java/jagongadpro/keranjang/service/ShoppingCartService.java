package jagongadpro.keranjang.service;

import jagongadpro.keranjang.config.GameApiProperties;
import jagongadpro.keranjang.dto.GameResponse;
import jagongadpro.keranjang.dto.KeranjangResponse;
import jagongadpro.keranjang.dto.WebResponse;
import jagongadpro.keranjang.model.ShoppingCart;
import jagongadpro.keranjang.repository.ShoppingCartRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.core.ParameterizedTypeReference;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@Service
public class ShoppingCartService {

    private static final String EMAIL_NOT_FOUND_MESSAGE = "Keranjang dengan email tersebut tidak ditemukan.";
    private static final String ITEM_NOT_FOUND_MESSAGE = "Item tidak ditemukan dalam keranjang.";

    private final ShoppingCartRepository shoppingCartRepository;
    private final BillingStrategy discountStrategy;
    private final BillingStrategy normalStrategy;
    private final RestTemplate restTemplate;
    private final GameApiProperties gameApiProperties;

    private BillingStrategy billingStrategy;

    public ShoppingCartService(ShoppingCartRepository shoppingCartRepository,
                               @Qualifier("discountPricingStrategy") BillingStrategy discountStrategy,
                               @Qualifier("normalPricingStrategy") BillingStrategy normalStrategy,
                               RestTemplate restTemplate,
                               GameApiProperties gameApiProperties) {
        this.shoppingCartRepository = shoppingCartRepository;
        this.discountStrategy = discountStrategy;
        this.normalStrategy = normalStrategy;
        this.restTemplate = restTemplate;
        this.gameApiProperties = gameApiProperties;
        setBillingStrategy();
    }

    public void setBillingStrategy() {
        this.billingStrategy = LocalDate.now().getDayOfMonth() == 1 ? discountStrategy : normalStrategy;
    }

    @Scheduled(cron = "0 0 0 1 * ?")
    @Async
    public CompletableFuture<Void> applyDiscountsToAllCarts() {
        this.billingStrategy = discountStrategy; // Ganti strategi penagihan menjadi discountStrategy
        List<ShoppingCart> carts = shoppingCartRepository.findAll();
        for (ShoppingCart cart : carts) {
            Map<String, Double> itemPrices = getItemPrices();
            double totalPrice = discountStrategy.calculateTotal(cart.getItems(), itemPrices);
            cart.setTotalPrice(totalPrice);
            shoppingCartRepository.save(cart);
        }
        return CompletableFuture.completedFuture(null);
    }

    @Scheduled(cron = "0 0 0 2 * ?")
    public void resetBillingStrategyToNormal() {
        this.billingStrategy = normalStrategy;
    }

    public KeranjangResponse addItem(String email, String itemId, int quantity) {
        ShoppingCart cart = shoppingCartRepository.findByEmail(email);
        if (cart == null) {
            cart = new ShoppingCart(email);
            // Simpan keranjang baru terlebih dahulu
            cart = shoppingCartRepository.save(cart);
        }

        // Update atau tambah item dalam keranjang
        cart.getItems().merge(itemId, quantity, Integer::sum);

        // Hitung total harga
        Map<String, Double> itemPrices = getItemPrices();
        Map<String, Integer> itemQuantities = new HashMap<>();
        cart.getItems().forEach((key, value) -> itemQuantities.put(String.valueOf(key), value));

        double totalPrice = billingStrategy.calculateTotal(itemQuantities, itemPrices);
        cart.setTotalPrice(totalPrice);

        // Simpan kembali keranjang setelah menambahkan item
        cart = shoppingCartRepository.save(cart);

        return new KeranjangResponse(cart.getEmail(), cart.getItems(), cart.getTotalPrice());
    }

    public KeranjangResponse updateItem(String email, String itemId, int quantity) {
        ShoppingCart cart = shoppingCartRepository.findByEmail(email);
        if (cart == null || !cart.getItems().containsKey(itemId)) {
            throw new IllegalArgumentException(ITEM_NOT_FOUND_MESSAGE);
        }

        cart.getItems().put(itemId, quantity);

        Map<String, Double> itemPrices = getItemPrices();
        double totalPrice = billingStrategy.calculateTotal(cart.getItems(), itemPrices);
        cart.setTotalPrice(totalPrice);

        shoppingCartRepository.save(cart);
        return new KeranjangResponse(cart.getEmail(), cart.getItems(), cart.getTotalPrice());
    }

    public KeranjangResponse deleteItem(String email, String itemId) {
        ShoppingCart cart = shoppingCartRepository.findByEmail(email);
        if (cart == null) {
            throw new IllegalArgumentException(EMAIL_NOT_FOUND_MESSAGE);
        }

        cart.getItems().remove(itemId);

        Map<String, Double> itemPrices = getItemPrices();
        double totalPrice = billingStrategy.calculateTotal(cart.getItems(), itemPrices);
        cart.setTotalPrice(totalPrice);

        shoppingCartRepository.save(cart);
        return new KeranjangResponse(cart.getEmail(), cart.getItems(), cart.getTotalPrice());
    }

    public KeranjangResponse incrementItem(String email, String itemId) {
        return addItem(email, itemId, 1);
    }

    public KeranjangResponse decrementItem(String email, String itemId) {
        ShoppingCart cart = shoppingCartRepository.findByEmail(email);
        if (cart == null || !cart.getItems().containsKey(itemId)) {
            throw new IllegalArgumentException(ITEM_NOT_FOUND_MESSAGE);
        }

        int currentQuantity = cart.getItems().get(itemId);
        if (currentQuantity <= 1) {
            cart.getItems().remove(itemId);
        } else {
            cart.getItems().put(itemId, currentQuantity - 1);
        }

        Map<String, Double> itemPrices = getItemPrices();
        double totalPrice = billingStrategy.calculateTotal(cart.getItems(), itemPrices);
        cart.setTotalPrice(totalPrice);

        shoppingCartRepository.save(cart);
        return new KeranjangResponse(cart.getEmail(), cart.getItems(), cart.getTotalPrice());
    }

    public KeranjangResponse findCartByEmail(String email) {
        ShoppingCart cart = shoppingCartRepository.findByEmail(email);
        if (cart == null) {
            throw new IllegalArgumentException(EMAIL_NOT_FOUND_MESSAGE);
        }

        return new KeranjangResponse(cart.getEmail(), cart.getItems(), cart.getTotalPrice());
    }

    public KeranjangResponse clearCart(String email) {
        ShoppingCart cart = shoppingCartRepository.findByEmail(email);
        if (cart == null) {
            throw new IllegalArgumentException(EMAIL_NOT_FOUND_MESSAGE);
        }

        cart.getItems().clear();
        cart.setTotalPrice(0.0);

        shoppingCartRepository.save(cart);
        return new KeranjangResponse(cart.getEmail(), cart.getItems(), cart.getTotalPrice());
    }

    public KeranjangResponse createCart(String email) {
        ShoppingCart existingCart = shoppingCartRepository.findByEmail(email);
        if (existingCart != null) {
            throw new IllegalArgumentException("Keranjang dengan email ini sudah ada.");
        }

        ShoppingCart newCart = new ShoppingCart(email);
        shoppingCartRepository.save(newCart);
        return new KeranjangResponse(newCart.getEmail(), newCart.getItems(), newCart.getTotalPrice());
    }

    Map<String, Double> getItemPrices() {
        ResponseEntity<WebResponse<List<GameResponse>>> response = restTemplate.exchange(
                gameApiProperties.getUrl(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<WebResponse<List<GameResponse>>>() {}
        );

        List<GameResponse> games = (response != null && response.getBody() != null && response.getBody().getData() != null)
                ? response.getBody().getData()
                : new ArrayList<>();
        Map<String, Double> itemPrices = new HashMap<>();
        for (GameResponse game : games) {
            itemPrices.put(game.getId(), game.getHarga().doubleValue()); // Gunakan ID sebagai kunci
        }
        return itemPrices;
    }
}
