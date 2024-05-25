package jagongadpro.keranjang.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

public class ShoppingCartTest {

    @Test
    public void testDefaultConstructor() {
        ShoppingCart cart = new ShoppingCart();
        assertNotNull(cart.getItems());
        assertTrue(cart.getItems().isEmpty());
        assertEquals(0.0, cart.getTotalPrice());
    }

    @Test
    public void testConstructorWithEmail() {
        ShoppingCart cart = new ShoppingCart("test@example.com");
        assertEquals("test@example.com", cart.getEmail());
        assertNotNull(cart.getItems());
        assertTrue(cart.getItems().isEmpty());
        assertEquals(0.0, cart.getTotalPrice());
    }

    @Test
    public void testSetEmail() {
        ShoppingCart cart = new ShoppingCart();
        cart.setEmail("test@example.com");
        assertEquals("test@example.com", cart.getEmail());
    }

    @Test
    public void testSetItems() {
        ShoppingCart cart = new ShoppingCart();
        Map<String, Integer> items = new HashMap<>();
        items.put("item1", 2);
        cart.setItems(items);
        assertEquals(items, cart.getItems());
    }

    @Test
    public void testSetTotalPrice() {
        ShoppingCart cart = new ShoppingCart();
        cart.setTotalPrice(100.0);
        assertEquals(100.0, cart.getTotalPrice());
    }
}
