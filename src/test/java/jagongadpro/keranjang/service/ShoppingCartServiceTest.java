package jagongadpro.keranjang.service;

import jagongadpro.keranjang.config.GameApiProperties;
import jagongadpro.keranjang.dto.GameResponse;
import jagongadpro.keranjang.dto.KeranjangResponse;
import jagongadpro.keranjang.dto.WebResponse;
import jagongadpro.keranjang.model.ShoppingCart;
import jagongadpro.keranjang.repository.ShoppingCartRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class ShoppingCartServiceTest {

    @Mock
    private ShoppingCartRepository shoppingCartRepository;

    @Mock
    private BillingStrategy discountStrategy;

    @Mock
    private BillingStrategy normalStrategy;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private GameApiProperties gameApiProperties;

    @InjectMocks
    private ShoppingCartService shoppingCartService;

    private ShoppingCart cart;
    private GameResponse gameResponse;
    private List<GameResponse> gameResponses;
    private WebResponse<List<GameResponse>> webResponse;
    private Map<String, Double> itemPrices;

    @SuppressWarnings("unchecked")
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        cart = new ShoppingCart("test@example.com");
        cart.getItems().put("item1", 1);
        cart.setTotalPrice(10000);

        gameResponse = new GameResponse();
        gameResponse.setId("8eb561a5-eed0-416e-965b-9b318ee1869e");
        gameResponse.setNama("Game 2");
        gameResponse.setDeskripsi("Deskripsi Game");
        gameResponse.setHarga(10000);
        gameResponse.setKategori("Kategori Game");
        gameResponse.setStok(10);
        gameResponses = Collections.singletonList(gameResponse);
        webResponse = new WebResponse<>(gameResponses, null);
        itemPrices = new HashMap<>();
        itemPrices.put("8eb561a5-eed0-416e-965b-9b318ee1869e", 10000.0);

        when(gameApiProperties.getUrl()).thenReturn("http://api.example.com");
        when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(), any(ParameterizedTypeReference.class)))
                .thenReturn(ResponseEntity.ok(webResponse));
        when(shoppingCartRepository.findByEmail(anyString())).thenReturn(cart);
        when(shoppingCartRepository.save(any(ShoppingCart.class))).thenAnswer(invocation -> invocation.getArgument(0));
    }

    @Test
    public void testAddItem() {
        when(discountStrategy.calculateTotal(any(), any())).thenReturn(20000.0);

        KeranjangResponse response = shoppingCartService.addItem("test@example.com", "item1", 1);

        assertEquals("test@example.com", response.getEmail());
        assertEquals(2, response.getItems().get("item1"));
    }

    @Test
    public void testAddItemNewCart() {
        when(shoppingCartRepository.findByEmail(anyString())).thenReturn(null);
        when(discountStrategy.calculateTotal(any(), any())).thenReturn(10000.0);

        KeranjangResponse response = shoppingCartService.addItem("newuser@example.com", "item1", 1);

        assertEquals("newuser@example.com", response.getEmail());
        assertEquals(1, response.getItems().get("item1"));

    }

    @Test
    public void testUpdateItem() {
        when(discountStrategy.calculateTotal(any(), any())).thenReturn(30000.0);

        KeranjangResponse response = shoppingCartService.updateItem("test@example.com", "item1", 3);

        assertEquals("test@example.com", response.getEmail());
        assertEquals(3, response.getItems().get("item1"));
    }

    @Test
    public void testUpdateItemNotFound() {
        when(shoppingCartRepository.findByEmail(anyString())).thenReturn(null);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            shoppingCartService.updateItem("test@example.com", "item1", 3);
        });

        assertEquals("Item tidak ditemukan dalam keranjang.", exception.getMessage());
    }

    @Test
    public void testUpdateItemItemNotFound() {
        cart.getItems().clear();

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            shoppingCartService.updateItem("test@example.com", "item1", 3);
        });

        assertEquals("Item tidak ditemukan dalam keranjang.", exception.getMessage());
    }

    @Test
    public void testDeleteItem() {
        when(discountStrategy.calculateTotal(any(), any())).thenReturn(0.0);

        KeranjangResponse response = shoppingCartService.deleteItem("test@example.com", "item1");

        assertEquals("test@example.com", response.getEmail());
        assertFalse(response.getItems().containsKey("item1"));
        assertEquals(0.0, response.getTotalPrice());
    }

    @Test
    public void testDeleteItemNotFound() {
        when(shoppingCartRepository.findByEmail(anyString())).thenReturn(null);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            shoppingCartService.deleteItem("test@example.com", "item1");
        });

        assertEquals("Keranjang dengan email tersebut tidak ditemukan.", exception.getMessage());
    }

    @Test
    public void testIncrementItem() {
        when(discountStrategy.calculateTotal(any(), any())).thenReturn(20000.0);

        KeranjangResponse response = shoppingCartService.incrementItem("test@example.com", "item1");

        assertEquals("test@example.com", response.getEmail());
        assertEquals(2, response.getItems().get("item1"));
    }

    @Test
    public void testDecrementItemRemoveItem() {
        when(discountStrategy.calculateTotal(any(), any())).thenReturn(0.0);
        cart.getItems().put("item1", 1);

        KeranjangResponse response = shoppingCartService.decrementItem("test@example.com", "item1");

        assertEquals("test@example.com", response.getEmail());
        assertFalse(response.getItems().containsKey("item1"));
        assertEquals(0.0, response.getTotalPrice());
    }

    @Test
    public void testDecrementItemDecreaseQuantity() {
        when(discountStrategy.calculateTotal(any(), any())).thenReturn(10000.0);
        cart.getItems().put("item1", 2);

        KeranjangResponse response = shoppingCartService.decrementItem("test@example.com", "item1");

        assertEquals("test@example.com", response.getEmail());
        assertTrue(response.getItems().containsKey("item1"));
        assertEquals(1, response.getItems().get("item1"));
    }

    @Test
    public void testDecrementItemNotFound() {
        when(shoppingCartRepository.findByEmail(anyString())).thenReturn(null);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            shoppingCartService.decrementItem("test@example.com", "item1");
        });

        assertEquals("Item tidak ditemukan dalam keranjang.", exception.getMessage());
    }

    @Test
    public void testDecrementItemItemNotFound() {
        cart.getItems().clear();

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            shoppingCartService.decrementItem("test@example.com", "item1");
        });

        assertEquals("Item tidak ditemukan dalam keranjang.", exception.getMessage());
    }

    @Test
    public void testFindCartByEmailNotFound() {
        when(shoppingCartRepository.findByEmail(anyString())).thenReturn(null);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            shoppingCartService.findCartByEmail("test@example.com");
        });

        assertEquals("Keranjang dengan email tersebut tidak ditemukan.", exception.getMessage());
    }

    @Test
    public void testClearCartNotFound() {
        when(shoppingCartRepository.findByEmail(anyString())).thenReturn(null);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            shoppingCartService.clearCart("test@example.com");
        });

        assertEquals("Keranjang dengan email tersebut tidak ditemukan.", exception.getMessage());
    }

    @Test
    public void testFindCartByEmail() {
        KeranjangResponse response = shoppingCartService.findCartByEmail("test@example.com");

        assertEquals("test@example.com", response.getEmail());
        assertEquals(1, response.getItems().get("item1"));
        assertEquals(10000.0, response.getTotalPrice());
    }

    @Test
    public void testClearCart() {
        when(discountStrategy.calculateTotal(any(), any())).thenReturn(0.0);

        KeranjangResponse response = shoppingCartService.clearCart("test@example.com");

        assertEquals("test@example.com", response.getEmail());
        assertTrue(response.getItems().isEmpty());
        assertEquals(0.0, response.getTotalPrice());
    }

    @Test
    public void testApplyDiscountsToAllCarts() throws Exception {
        ShoppingCart anotherCart = new ShoppingCart("another@example.com");
        anotherCart.getItems().put("item2", 1);
        anotherCart.setTotalPrice(20000);

        when(shoppingCartRepository.findAll()).thenReturn(Arrays.asList(cart, anotherCart));
        when(discountStrategy.calculateTotal(any(), any())).thenReturn(5000.0, 15000.0);

        CompletableFuture<Void> future = shoppingCartService.applyDiscountsToAllCarts();
        future.get();

        verify(shoppingCartRepository, times(1)).save(cart);
        verify(shoppingCartRepository, times(1)).save(anotherCart);

    }
}
