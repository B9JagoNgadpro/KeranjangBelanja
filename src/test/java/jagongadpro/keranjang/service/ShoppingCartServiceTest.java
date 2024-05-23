package jagongadpro.keranjang.service;

import jagongadpro.keranjang.dto.KeranjangResponse;
import jagongadpro.keranjang.dto.GameResponse;
import jagongadpro.keranjang.dto.WebResponse;
import jagongadpro.keranjang.model.ShoppingCart;
import jagongadpro.keranjang.repository.ShoppingCartRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SuppressWarnings("unchecked")
public class ShoppingCartServiceTest {

    @Mock
    private ShoppingCartRepository shoppingCartRepository;

    @Mock
    @Qualifier("discountPricingStrategy")
    private BillingStrategy discountStrategy;

    @Mock
    @Qualifier("normalPricingStrategy")
    private BillingStrategy normalStrategy;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private ShoppingCartService shoppingCartService;

    private String currentTestEmail;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        shoppingCartService.setBillingStrategy();
    }

    @AfterEach
    public void tearDown() {
        if (currentTestEmail != null) {
            ShoppingCart cart = shoppingCartRepository.findByEmail(currentTestEmail);
            if (cart != null) {
                shoppingCartRepository.delete(cart);
            }
        }
    }

    @Test
    public void testAddItem() {
        currentTestEmail = "test1@example.com";
        String itemId = "item1";
        int quantity = 2;

        ShoppingCart cart = new ShoppingCart(currentTestEmail);

        when(shoppingCartRepository.findByEmail(currentTestEmail)).thenReturn(cart);

        Map<String, Double> itemPrices = new HashMap<>();
        itemPrices.put(itemId, 10.0);

        GameResponse gameResponse = new GameResponse();
        gameResponse.setNama(itemId);
        gameResponse.setHarga(10);

        WebResponse<List<GameResponse>> webResponse = new WebResponse<>();
        webResponse.setData(List.of(gameResponse));

        when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(), any(ParameterizedTypeReference.class)))
                .thenReturn(ResponseEntity.ok(webResponse));

        when(normalStrategy.calculateTotal(any(), any())).thenReturn(20.0);

        KeranjangResponse response = shoppingCartService.addItem(currentTestEmail, itemId, quantity);

        assertEquals(currentTestEmail, response.getEmail());
        assertEquals(1, response.getItems().size());
        assertEquals(20.0, response.getTotalPrice());

        verify(shoppingCartRepository, times(1)).save(any(ShoppingCart.class));
    }

    @Test
    public void testAddItemKeranjangNotFound() {
        currentTestEmail = "test2@example.com";
        String itemId = "item1";
        int quantity = 2;

        when(shoppingCartRepository.findByEmail(currentTestEmail)).thenReturn(null);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            shoppingCartService.addItem(currentTestEmail, itemId, quantity);
        });

        assertEquals("Keranjang dengan email tersebut tidak ditemukan.", exception.getMessage());
    }

    @Test
    public void testUpdateItem() {
        currentTestEmail = "test3@example.com";
        String itemId = "item1";
        int quantity = 3;

        ShoppingCart cart = new ShoppingCart(currentTestEmail);
        cart.getItems().put(itemId, 2);

        when(shoppingCartRepository.findByEmail(currentTestEmail)).thenReturn(cart);

        Map<String, Double> itemPrices = new HashMap<>();
        itemPrices.put(itemId, 10.0);

        GameResponse gameResponse = new GameResponse();
        gameResponse.setNama(itemId);
        gameResponse.setHarga(10);

        WebResponse<List<GameResponse>> webResponse = new WebResponse<>();
        webResponse.setData(List.of(gameResponse));

        when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(), any(ParameterizedTypeReference.class)))
                .thenReturn(ResponseEntity.ok(webResponse));

        when(normalStrategy.calculateTotal(any(), any())).thenReturn(30.0);

        KeranjangResponse response = shoppingCartService.updateItem(currentTestEmail, itemId, quantity);

        assertEquals(currentTestEmail, response.getEmail());
        assertEquals(1, response.getItems().size());
        assertEquals(0.0, response.getTotalPrice());

        verify(shoppingCartRepository, times(1)).save(any(ShoppingCart.class));
    }

    @Test
    public void testUpdateItemNotFound() {
        currentTestEmail = "test4@example.com";
        String itemId = "item1";
        int quantity = 3;

        ShoppingCart cart = new ShoppingCart(currentTestEmail);

        when(shoppingCartRepository.findByEmail(currentTestEmail)).thenReturn(cart);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            shoppingCartService.updateItem(currentTestEmail, itemId, quantity);
        });

        assertEquals("Item tidak ditemukan dalam keranjang.", exception.getMessage());
    }

    @Test
    public void testDeleteItem() {
        currentTestEmail = "test5@example.com";
        String itemId = "item1";

        ShoppingCart cart = new ShoppingCart(currentTestEmail);
        cart.getItems().put(itemId, 2);

        when(shoppingCartRepository.findByEmail(currentTestEmail)).thenReturn(cart);

        Map<String, Double> itemPrices = new HashMap<>();
        itemPrices.put(itemId, 10.0);

        GameResponse gameResponse = new GameResponse();
        gameResponse.setNama(itemId);
        gameResponse.setHarga(10);

        WebResponse<List<GameResponse>> webResponse = new WebResponse<>();
        webResponse.setData(List.of(gameResponse));

        when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(), any(ParameterizedTypeReference.class)))
                .thenReturn(ResponseEntity.ok(webResponse));

        when(normalStrategy.calculateTotal(any(), any())).thenReturn(0.0);

        shoppingCartService.deleteItem(currentTestEmail, itemId);

        assertEquals(0, cart.getItems().size());
        assertEquals(0.0, cart.getTotalPrice());

        verify(shoppingCartRepository, times(1)).save(any(ShoppingCart.class));
    }

    @Test
    public void testDeleteItemKeranjangNotFound() {
        currentTestEmail = "test6@example.com";
        String itemId = "item1";

        when(shoppingCartRepository.findByEmail(currentTestEmail)).thenReturn(null);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            shoppingCartService.deleteItem(currentTestEmail, itemId);
        });

        assertEquals("Keranjang dengan email tersebut tidak ditemukan.", exception.getMessage());
    }

    @Test
    public void testFindCartByEmail() {
        currentTestEmail = "test7@example.com";

        ShoppingCart cart = new ShoppingCart(currentTestEmail);
        cart.setTotalPrice(100.0);

        when(shoppingCartRepository.findByEmail(currentTestEmail)).thenReturn(cart);

        KeranjangResponse response = shoppingCartService.findCartByEmail(currentTestEmail);

        assertEquals(currentTestEmail, response.getEmail());
        assertEquals(100.0, response.getTotalPrice());
    }

    @Test
    public void testFindCartByEmailNotFound() {
        currentTestEmail = "test8@example.com";

        when(shoppingCartRepository.findByEmail(currentTestEmail)).thenReturn(null);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            shoppingCartService.findCartByEmail(currentTestEmail);
        });

        assertEquals("Keranjang dengan email tersebut tidak ditemukan.", exception.getMessage());
    }

    @Test
    public void testClearCart() {
        currentTestEmail = "test9@example.com";

        ShoppingCart cart = new ShoppingCart(currentTestEmail);
        cart.setTotalPrice(100.0);

        when(shoppingCartRepository.findByEmail(currentTestEmail)).thenReturn(cart);

        shoppingCartService.clearCart(currentTestEmail);

        assertEquals(0, cart.getItems().size());
        assertEquals(0.0, cart.getTotalPrice());

        verify(shoppingCartRepository, times(1)).save(any(ShoppingCart.class));
    }

    @Test
    public void testClearCartNotFound() {
        currentTestEmail = "test10@example.com";

        when(shoppingCartRepository.findByEmail(currentTestEmail)).thenReturn(null);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            shoppingCartService.clearCart(currentTestEmail);
        });

        assertEquals("Keranjang dengan email tersebut tidak ditemukan.", exception.getMessage());
    }

    @Test
    public void testApplyDiscountsToAllCarts() {
        currentTestEmail = "test11@example.com";
        ShoppingCart cart1 = new ShoppingCart(currentTestEmail);
        cart1.setItems(new HashMap<>());
        cart1.getItems().put("item1", 2);

        ShoppingCart cart2 = new ShoppingCart("user2@example.com");
        cart2.setItems(new HashMap<>());
        cart2.getItems().put("item2", 3);

        when(shoppingCartRepository.findAll()).thenReturn(List.of(cart1, cart2));

        GameResponse gameResponse1 = new GameResponse();
        gameResponse1.setNama("item1");
        gameResponse1.setHarga(10);

        GameResponse gameResponse2 = new GameResponse();
        gameResponse2.setNama("item2");
        gameResponse2.setHarga(20);

        WebResponse<List<GameResponse>> webResponse = new WebResponse<>();
        webResponse.setData(List.of(gameResponse1, gameResponse2));

        when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(), any(ParameterizedTypeReference.class)))
                .thenReturn(ResponseEntity.ok(webResponse));

        Map<String, Double> itemPrices = new HashMap<>();
        itemPrices.put("item1", 10.0);
        itemPrices.put("item2", 20.0);

        when(discountStrategy.calculateTotal(eq(cart1.getItems()), eq(itemPrices))).thenReturn(20.0);
        when(discountStrategy.calculateTotal(eq(cart2.getItems()), eq(itemPrices))).thenReturn(60.0);

        shoppingCartService.applyDiscountsToAllCarts();

        assertEquals(20.0, cart1.getTotalPrice());
        assertEquals(60.0, cart2.getTotalPrice());

        verify(shoppingCartRepository, times(1)).save(cart1);
        verify(shoppingCartRepository, times(1)).save(cart2);
    }

    @Test
    public void testApplyDiscountsToAllCartsAsync() throws Exception {
        currentTestEmail = "test12@example.com";
        ShoppingCart cart1 = new ShoppingCart("user1@example.com");
        cart1.setItems(new HashMap<>());
        cart1.getItems().put("item1", 2);

        ShoppingCart cart2 = new ShoppingCart("user2@example.com");
        cart2.setItems(new HashMap<>());
        cart2.getItems().put("item2", 3);

        when(shoppingCartRepository.findAll()).thenReturn(List.of(cart1, cart2));

        GameResponse gameResponse1 = new GameResponse();
        gameResponse1.setNama("item1");
        gameResponse1.setHarga(10);

        GameResponse gameResponse2 = new GameResponse();
        gameResponse2.setNama("item2");
        gameResponse2.setHarga(20);

        WebResponse<List<GameResponse>> webResponse = new WebResponse<>();
        webResponse.setData(List.of(gameResponse1, gameResponse2));

        when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(), any(ParameterizedTypeReference.class)))
                .thenReturn(ResponseEntity.ok(webResponse));

        Map<String, Double> itemPrices = new HashMap<>();
        itemPrices.put("item1", 10.0);
        itemPrices.put("item2", 20.0);

        when(discountStrategy.calculateTotal(eq(cart1.getItems()), eq(itemPrices))).thenReturn(20.0);
        when(discountStrategy.calculateTotal(eq(cart2.getItems()), eq(itemPrices))).thenReturn(60.0);

        CompletableFuture<Void> future = shoppingCartService.applyDiscountsToAllCarts();

        // Tunggu sebentar untuk memastikan metode berjalan asinkron
        Thread.sleep(100);

        // Verifikasi bahwa metode applyDiscountsToAllCarts berjalan asinkron
        assertTrue(future.isDone());

        future.join();

        // Verifikasi bahwa totalPrice diperbarui dengan benar di objek cart
        assertEquals(0.0, cart1.getTotalPrice());
        assertEquals(0.0, cart2.getTotalPrice());

        // Verifikasi bahwa objek ShoppingCart disimpan dengan benar
        verify(shoppingCartRepository, times(1)).save(cart1);
        verify(shoppingCartRepository, times(1)).save(cart2);
    }

}
