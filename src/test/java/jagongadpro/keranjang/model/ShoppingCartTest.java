package jagongadpro.keranjang.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ShoppingCartTest {
    private ShoppingCart cart;

    @BeforeEach
    void setup() {
        // Reset instance ShoppingCart untuk setiap test
        ShoppingCart.resetInstance();
        cart = ShoppingCart.getInstance();
    }

    @Test
    void testSingletonInstance() {
        ShoppingCart anotherCart = ShoppingCart.getInstance();
        assertSame(cart, anotherCart, "Kedua variabel harus mengacu pada instance yang sama.");
    }

    @Test
    void testAddItemToCart() {
        cart.addItem("game001", 1);
        assertEquals(1, cart.getItems().size(), "Keranjang seharusnya memiliki satu item.");
    }

    @Test
    void testRemoveItemFromCart() {
        cart.addItem("game001", 1);
        cart.removeItem("game001");
        assertTrue(cart.getItems().isEmpty(), "Keranjang seharusnya kosong setelah item dihapus.");
    }

    @Test
    void testUpdateItemQuantity() {
        cart.addItem("game001", 1);
        cart.updateItem("game001", 3);
        int quantity = cart.getItems().get("game001");
        assertEquals(3, quantity, "Kuantitas item harus diperbarui menjadi 3.");
    }

    @Test
    void testCalculateTotalPrice() {
        cart.addItem("game001", 1);  // Misalkan harga per unit adalah 50000
        cart.addItem("game002", 2);  // Misalkan harga per unit adalah 30000
        double totalPrice = cart.calculateTotalPrice();
        assertEquals(110000, totalPrice, "Total harga harus dihitung dengan benar.");
    }
}
