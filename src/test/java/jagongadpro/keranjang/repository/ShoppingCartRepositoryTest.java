package jagongadpro.keranjang.repository;

import jagongadpro.keranjang.model.ShoppingCart;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ShoppingCartRepositoryTest {

    private ShoppingCartRepository repository;

    @BeforeEach
    public void setup() {
        // Reset Singleton
        ShoppingCartRepository.resetInstance();
        repository = ShoppingCartRepository.getInstance();
    }

    @Test
    public void testSingletonInstance() {
        ShoppingCartRepository anotherInstance = ShoppingCartRepository.getInstance();
        assertSame(repository, anotherInstance);
    }

    @Test
    public void testAddItem() {
        ShoppingCart cart = repository.addItem(101, 2);
        assertNotNull(cart);
        assertEquals(2, cart.getQuantity());
    }

    @Test
    public void testGetItem() {
        repository.addItem(101, 2);
        ShoppingCart cart = repository.getItem(101);
        assertNotNull(cart);
        assertEquals(2, cart.getQuantity());
    }

    @Test
    public void testUpdateItem() {
        repository.addItem(101, 2);
        ShoppingCart updatedCart = repository.updateItem(101, 5);
        assertNotNull(updatedCart);
        assertEquals(5, updatedCart.getQuantity());
    }

    @Test
    public void testRemoveItem() {
        repository.addItem(101, 3);
        repository.removeItem(101);
        ShoppingCart cart = repository.getItem(101);
        assertNull(cart);
    }
}
