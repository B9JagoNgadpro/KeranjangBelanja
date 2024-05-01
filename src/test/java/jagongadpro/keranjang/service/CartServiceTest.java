package jagongadpro.keranjang.service;

import jagongadpro.keranjang.dto.KeranjangResponse;
import jagongadpro.keranjang.model.ShoppingCart;
import jagongadpro.keranjang.repository.ShoppingCartRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {
    @InjectMocks
    ShoppingCartService shoppingCartService;

    @Mock
    ShoppingCartRepository shoppingCartRepository;

    @Mock
    BillingStrategy billingStrategy;

    @Mock
    PricingService pricingService;

    @Test
    void updateItemSuccess() {
        String email = "user@example.com";
        int itemId = 101;
        int quantity = 10;
        ShoppingCart cart = new ShoppingCart(email);
        cart.getItems().put(itemId, 5);
        when(shoppingCartRepository.findByEmail(email)).thenReturn(cart);
        when(billingStrategy.calculateTotal(any(), any())).thenReturn(200.0);

        KeranjangResponse response = shoppingCartService.updateItem(email, itemId, quantity);

        assertNotNull(response);
        assertEquals(200.0, response.getTotalPrice());
        verify(shoppingCartRepository).save(cart);
    }

    @Test
    void deleteItemSuccess() {
        String email = "user@example.com";
        int itemId = 101;
        ShoppingCart cart = new ShoppingCart(email);
        cart.getItems().put(itemId, 5);
        when(shoppingCartRepository.findByEmail(email)).thenReturn(cart);

        shoppingCartService.deleteItem(email, itemId);

        assertTrue(cart.getItems().isEmpty());
        verify(shoppingCartRepository).save(cart);
    }

    @Test
    void findCartByEmailSuccess() {
        String email = "user@example.com";
        ShoppingCart cart = new ShoppingCart(email);
        cart.getItems().put(101, 5);
        when(shoppingCartRepository.findByEmail(email)).thenReturn(cart);

        KeranjangResponse response = shoppingCartService.findCartByEmail(email);

        assertNotNull(response);
        assertEquals(email, response.getEmail());
    }

    @Test
    void clearCartSuccess() {
        String email = "user@example.com";
        ShoppingCart cart = new ShoppingCart(email);
        cart.getItems().put(101, 10);
        when(shoppingCartRepository.findByEmail(email)).thenReturn(cart);

        shoppingCartService.clearCart(email);

        assertTrue(cart.getItems().isEmpty());
        verify(shoppingCartRepository).save(cart);
    }

    @Test
    void findCartByEmailNotFound() {
        String email = "nonexistent@example.com";
        when(shoppingCartRepository.findByEmail(email)).thenReturn(null);

        assertThrows(ResponseStatusException.class, () -> shoppingCartService.findCartByEmail(email));
    }
}
