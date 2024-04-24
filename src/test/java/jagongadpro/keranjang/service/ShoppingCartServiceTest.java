package jagongadpro.keranjang.service;

import jagongadpro.keranjang.model.ShoppingCart;
import jagongadpro.keranjang.repository.ShoppingCartRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ShoppingCartServiceTest {

    private ShoppingCartService shoppingCartService;

    @Mock
    private ShoppingCartRepository shoppingCartRepository;

    @BeforeEach
    void setUp() {
        shoppingCartService = new ShoppingCartService(shoppingCartRepository);
    }

    @Test
    void testAddItem() {
        // Setup
        ShoppingCart cart = new ShoppingCart(101, 5);
        when(shoppingCartRepository.addItem(101, 5)).thenReturn(cart);

        // Execution
        ShoppingCart result = shoppingCartService.addItem(101, 5);

        // Assertions
        assertEquals(cart, result);
        verify(shoppingCartRepository, times(1)).addItem(101, 5);
    }

    @Test
    void testFindAllItems() {
        // Setup
        ShoppingCart cart1 = new ShoppingCart(101, 5);
        ShoppingCart cart2 = new ShoppingCart(102, 3);
        Map<Integer, ShoppingCart> items = new HashMap<>();
        items.put(101, cart1);
        items.put(102, cart2);
        when(shoppingCartRepository.getAllItems()).thenReturn(items);

        // Execution
        List<ShoppingCart> result = shoppingCartService.findAllItems();

        // Assertions
        assertEquals(2, result.size());
        assertTrue(result.contains(cart1) && result.contains(cart2));
        verify(shoppingCartRepository, times(1)).getAllItems();
    }

    @Test
    void testUpdateItem() {
        // Setup
        ShoppingCart cart = new ShoppingCart(101, 10);
        when(shoppingCartRepository.updateItem(101, 10)).thenReturn(cart);

        // Execution
        ShoppingCart result = shoppingCartService.updateItem(101, 10);

        // Assertions
        assertEquals(cart, result);
        verify(shoppingCartRepository, times(1)).updateItem(101, 10);
    }

    @Test
    void testDeleteItem() {
        // Setup
        doNothing().when(shoppingCartRepository).removeItem(101);

        // Execution
        shoppingCartService.deleteItem(101);

        // Verification
        verify(shoppingCartRepository, times(1)).removeItem(101);
    }

    @Test
    void testFindItemById() {
        // Setup
        ShoppingCart cart = new ShoppingCart(101, 5);
        when(shoppingCartRepository.getItem(101)).thenReturn(cart);

        // Execution
        ShoppingCart result = shoppingCartService.findItemById(101);

        // Assertions
        assertEquals(cart, result);
        verify(shoppingCartRepository, times(1)).getItem(101);
    }
}
