package jagongadpro.keranjang.model;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class ShoppingCartTest {

    @Test
    public void testShoppingCartConstructorAndGetters() {
        String email = "test@example.com";
        ShoppingCart cart = new ShoppingCart(email);

        assertThat(cart.getEmail()).isEqualTo(email);
        assertThat(cart.getItems()).isEmpty();
        assertThat(cart.getTotalPrice()).isEqualTo(0.0);
    }

    @Test
    public void testShoppingCartSetters() {
        String email = "test@example.com";
        ShoppingCart cart = new ShoppingCart(email);

        Map<String, Integer> items = new HashMap<>();
        items.put("item1", 2);
        items.put("item2", 3);
        cart.setItems(items);

        cart.setTotalPrice(50.0);

        assertThat(cart.getItems()).hasSize(2);
        assertThat(cart.getItems().get("item1")).isEqualTo(2);
        assertThat(cart.getItems().get("item2")).isEqualTo(3);
        assertThat(cart.getTotalPrice()).isEqualTo(50.0);
    }
}
