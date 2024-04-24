package jagongadpro.keranjang.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ShoppingCartTest {

    @Test
    public void testCreateShoppingCartItem() {
        ShoppingCart cart = new ShoppingCart(101, 5);
        assertEquals(101, cart.getItemId());
        assertEquals(5, cart.getQuantity());
    }

    @Test
    public void testSetItemQuantity() {
        ShoppingCart cart = new ShoppingCart(101, 5);
        cart.setQuantity(10);
        assertEquals(10, cart.getQuantity());
    }
}
