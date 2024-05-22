package jagongadpro.keranjang.service;

import jagongadpro.keranjang.dto.KeranjangResponse;
import jagongadpro.keranjang.dto.GameResponse;
import jagongadpro.keranjang.dto.WebResponse;
import jagongadpro.keranjang.model.ShoppingCart;
import jagongadpro.keranjang.repository.ShoppingCartRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SuppressWarnings("unchecked")
@SpringBootTest
@EnableAsync
@EnableScheduling
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

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        shoppingCartService.setBillingStrategy();
    }

    @Test
    public void testAddItem() {
        String email = "test@example.com";
        String itemId = "item1";
        int quantity = 2;

        ShoppingCart cart = new ShoppingCart(email);
        cart.setItems(new HashMap<>());

        when(shoppingCartRepository.findByEmail(email)).thenReturn(cart);

        GameResponse gameResponse = new GameResponse();
        gameResponse.setNama(itemId);
        gameResponse.setHarga(10);

        WebResponse<List<GameResponse>> webResponse = new WebResponse<>();
        webResponse.setData(List.of(gameResponse));

        when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(), any(ParameterizedTypeReference.class)))
                .thenReturn(ResponseEntity.ok(webResponse));

        Map<String, Double> itemPrices = new HashMap<>();
        itemPrices.put(itemId, 10.0);

        when(normalStrategy.calculateTotal(eq(cart.getItems()), eq(itemPrices))).thenReturn(20.0);

        KeranjangResponse response = shoppingCartService.addItem(email, itemId, quantity);

        // Verifikasi bahwa calculateTotal dipanggil dengan benar
        verify(normalStrategy, times(1)).calculateTotal(eq(cart.getItems()), eq(itemPrices));

        // Verifikasi bahwa totalPrice diperbarui dengan benar di objek cart
        assertEquals(20.0, cart.getTotalPrice());

        // Verifikasi bahwa objek ShoppingCart disimpan dengan benar
        verify(shoppingCartRepository, times(1)).save(cart);

        // Verifikasi hasil response
        assertEquals(email, response.getEmail());
        assertEquals(1, response.getItems().size());
        assertEquals(20.0, response.getTotalPrice());
    }

    @Test
    public void testUpdateItem() {
        String email = "test@example.com";
        String itemId = "item1";
        int quantity = 3;

        ShoppingCart cart = new ShoppingCart(email);
        cart.setItems(new HashMap<>());
        cart.getItems().put(itemId, 2);

        when(shoppingCartRepository.findByEmail(email)).thenReturn(cart);

        GameResponse gameResponse = new GameResponse();
        gameResponse.setNama(itemId);
        gameResponse.setHarga(10);

        WebResponse<List<GameResponse>> webResponse = new WebResponse<>();
        webResponse.setData(List.of(gameResponse));

        when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(), any(ParameterizedTypeReference.class)))
                .thenReturn(ResponseEntity.ok(webResponse));

        Map<String, Double> itemPrices = new HashMap<>();
        itemPrices.put(itemId, 10.0);

        when(normalStrategy.calculateTotal(eq(cart.getItems()), eq(itemPrices))).thenReturn(30.0);

        KeranjangResponse response = shoppingCartService.updateItem(email, itemId, quantity);

        // Verifikasi bahwa calculateTotal dipanggil dengan benar
        verify(normalStrategy, times(1)).calculateTotal(eq(cart.getItems()), eq(itemPrices));

        // Verifikasi bahwa totalPrice diperbarui dengan benar di objek cart
        assertEquals(30.0, cart.getTotalPrice());

        // Verifikasi bahwa objek ShoppingCart disimpan dengan benar
        verify(shoppingCartRepository, times(1)).save(cart);

        // Verifikasi hasil response
        assertEquals(email, response.getEmail());
        assertEquals(1, response.getItems().size());
        assertEquals(30.0, response.getTotalPrice());
    }

    @Test
    public void testDeleteItem() {
        String email = "test@example.com";
        String itemId = "item1";

        ShoppingCart cart = new ShoppingCart(email);
        cart.setItems(new HashMap<>());
        cart.getItems().put(itemId, 2);

        when(shoppingCartRepository.findByEmail(email)).thenReturn(cart);

        GameResponse gameResponse = new GameResponse();
        gameResponse.setNama(itemId);
        gameResponse.setHarga(10);

        WebResponse<List<GameResponse>> webResponse = new WebResponse<>();
        webResponse.setData(List.of(gameResponse));

        when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(), any(ParameterizedTypeReference.class)))
                .thenReturn(ResponseEntity.ok(webResponse));

        Map<String, Double> itemPrices = new HashMap<>();
        itemPrices.put(itemId, 10.0);

        when(normalStrategy.calculateTotal(eq(cart.getItems()), eq(itemPrices))).thenReturn(0.0);

        shoppingCartService.deleteItem(email, itemId);

        // Verifikasi bahwa calculateTotal dipanggil dengan benar
        verify(normalStrategy, times(1)).calculateTotal(eq(cart.getItems()), eq(itemPrices));

        // Verifikasi bahwa totalPrice diperbarui dengan benar di objek cart
        assertEquals(0.0, cart.getTotalPrice());

        // Verifikasi bahwa objek ShoppingCart disimpan dengan benar
        verify(shoppingCartRepository, times(1)).save(cart);
    }

    @Test
    public void testFindCartByEmail() {
        String email = "test@example.com";

        ShoppingCart cart = new ShoppingCart(email);
        cart.setTotalPrice(100.0);

        when(shoppingCartRepository.findByEmail(email)).thenReturn(cart);

        KeranjangResponse response = shoppingCartService.findCartByEmail(email);

        assertEquals(email, response.getEmail());
        assertEquals(100.0, response.getTotalPrice());
    }

    @Test
    public void testFindCartByEmailNotFound() {
        String email = "test@example.com";

        when(shoppingCartRepository.findByEmail(email)).thenReturn(null);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            shoppingCartService.findCartByEmail(email);
        });

        assertEquals("Keranjang dengan email tersebut tidak ditemukan.", exception.getMessage());
    }

    @Test
    public void testClearCart() {
        String email = "test@example.com";

        ShoppingCart cart = new ShoppingCart(email);
        cart.setItems(new HashMap<>());
        cart.getItems().put("item1", 2);
        cart.setTotalPrice(100.0);

        when(shoppingCartRepository.findByEmail(email)).thenReturn(cart);

        shoppingCartService.clearCart(email);

        // Verifikasi bahwa keranjang dihapus dengan benar
        assertTrue(cart.getItems().isEmpty());
        assertEquals(0.0, cart.getTotalPrice());

        // Verifikasi bahwa objek ShoppingCart disimpan dengan benar
        verify(shoppingCartRepository, times(1)).save(cart);
    }

    @Test
    public void testApplyDiscountsToAllCarts() {
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
        future.join();

        // Verifikasi bahwa calculateTotal dipanggil dengan benar
        verify(discountStrategy, times(1)).calculateTotal(eq(cart1.getItems()), eq(itemPrices));
        verify(discountStrategy, times(1)).calculateTotal(eq(cart2.getItems()), eq(itemPrices));

        // Verifikasi bahwa totalPrice diperbarui dengan benar di objek cart
        assertEquals(20.0, cart1.getTotalPrice());
        assertEquals(60.0, cart2.getTotalPrice());

        // Verifikasi bahwa objek ShoppingCart disimpan dengan benar
        verify(shoppingCartRepository, times(1)).save(cart1);
        verify(shoppingCartRepository, times(1)).save(cart2);
    }
}
