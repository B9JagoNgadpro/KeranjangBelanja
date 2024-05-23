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
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        when(shoppingCartRepository.findByEmail(email)).thenReturn(cart);

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

        KeranjangResponse response = shoppingCartService.addItem(email, itemId, quantity);

        assertEquals(email, response.getEmail());
        assertEquals(1, response.getItems().size());
        assertEquals(20.0, response.getTotalPrice());

        verify(shoppingCartRepository, times(1)).save(any(ShoppingCart.class));
    }

    @Test
    public void testAddItemKeranjangNotFound() {
        String email = "test@example.com";
        String itemId = "item1";
        int quantity = 2;

        when(shoppingCartRepository.findByEmail(email)).thenReturn(null);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            shoppingCartService.addItem(email, itemId, quantity);
        });

        assertEquals("Keranjang dengan email tersebut tidak ditemukan.", exception.getMessage());
    }

    @Test
    public void testUpdateItem() {
        String email = "test@example.com";
        String itemId = "item1";
        int quantity = 3;

        ShoppingCart cart = new ShoppingCart(email);
        cart.getItems().put(itemId, 2);

        when(shoppingCartRepository.findByEmail(email)).thenReturn(cart);

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

        KeranjangResponse response = shoppingCartService.updateItem(email, itemId, quantity);

        assertEquals(email, response.getEmail());
        assertEquals(1, response.getItems().size());
        assertEquals(0.0, response.getTotalPrice());

        verify(shoppingCartRepository, times(1)).save(any(ShoppingCart.class));
    }

    @Test
    public void testUpdateItemNotFound() {
        String email = "test@example.com";
        String itemId = "item1";
        int quantity = 3;

        ShoppingCart cart = new ShoppingCart(email);

        when(shoppingCartRepository.findByEmail(email)).thenReturn(cart);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            shoppingCartService.updateItem(email, itemId, quantity);
        });

        assertEquals("Item tidak ditemukan dalam keranjang.", exception.getMessage());
    }

    @Test
    public void testDeleteItem() {
        String email = "test@example.com";
        String itemId = "item1";

        ShoppingCart cart = new ShoppingCart(email);
        cart.getItems().put(itemId, 2);

        when(shoppingCartRepository.findByEmail(email)).thenReturn(cart);

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

        shoppingCartService.deleteItem(email, itemId);

        assertEquals(0, cart.getItems().size());
        assertEquals(0.0, cart.getTotalPrice());

        verify(shoppingCartRepository, times(1)).save(any(ShoppingCart.class));
    }

    @Test
    public void testDeleteItemKeranjangNotFound() {
        String email = "test@example.com";
        String itemId = "item1";

        when(shoppingCartRepository.findByEmail(email)).thenReturn(null);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            shoppingCartService.deleteItem(email, itemId);
        });

        assertEquals("Keranjang dengan email tersebut tidak ditemukan.", exception.getMessage());
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
        cart.setTotalPrice(100.0);

        when(shoppingCartRepository.findByEmail(email)).thenReturn(cart);

        shoppingCartService.clearCart(email);

        assertEquals(0, cart.getItems().size());
        assertEquals(0.0, cart.getTotalPrice());

        verify(shoppingCartRepository, times(1)).save(any(ShoppingCart.class));
    }

    @Test
    public void testClearCartNotFound() {
        String email = "test@example.com";

        when(shoppingCartRepository.findByEmail(email)).thenReturn(null);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            shoppingCartService.clearCart(email);
        });

        assertEquals("Keranjang dengan email tersebut tidak ditemukan.", exception.getMessage());
    }
}