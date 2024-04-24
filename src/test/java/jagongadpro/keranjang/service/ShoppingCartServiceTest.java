package jagongadpro.keranjang.service;

import jagongadpro.keranjang.model.ShoppingCart;
import jagongadpro.keranjang.repository.ShoppingCartRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ShoppingCartServiceTest {

    @Mock
    private ShoppingCartRepository shoppingCartRepository;

    @InjectMocks
    private ShoppingCartService shoppingCartService;

    @SuppressWarnings("deprecation")
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testAddItem() {
        ShoppingCartItem item = new ShoppingCartItem(101, 5);
        when(shoppingCartRepository.addItem(item)).thenReturn(item);
        
        ShoppingCartItem result = shoppingCartService.addItem(item);
        
        verify(shoppingCartRepository, times(1)).addItem(item);
        assertEquals(item, result);
    }

    @Test
    public void testFindAllItems() {
        ShoppingCartItem item1 = new ShoppingCartItem(101, 5);
        ShoppingCartItem item2 = new ShoppingCartItem(102, 3);
        List<ShoppingCartItem> itemList = Arrays.asList(item1, item2);
        when(shoppingCartRepository.findAllItems()).thenReturn(itemList.iterator());
        
        List<ShoppingCartItem> result = shoppingCartService.findAllItems();
        
        verify(shoppingCartRepository, times(1)).findAllItems();
        assertEquals(2, result.size());
        assertEquals(item1, result.get(0));
        assertEquals(item2, result.get(1));
    }

    @Test
    public void testUpdateItem() {
        ShoppingCartItem item = new ShoppingCartItem(101, 10);
        when(shoppingCartRepository.updateItem(101, item)).thenReturn(item);
        
        ShoppingCartItem result = shoppingCartService.updateItem(101, item);
        
        verify(shoppingCartRepository, times(1)).updateItem(101, item);
        assertEquals(item, result);
    }

    @Test
    public void testDeleteItem() {
        doNothing().when(shoppingCartRepository).removeItem(101);
        
        shoppingCartService.deleteItem(101);
        
        verify(shoppingCartRepository, times(1)).removeItem(101);
    }

    @Test
    public void testFindItemById() {
        ShoppingCartItem item = new ShoppingCartItem(101, 5);
        when(shoppingCartRepository.findItemById(101)).thenReturn(item);
        
        ShoppingCartItem result = shoppingCartService.findItemById(101);
        
        verify(shoppingCartRepository, times(1)).findItemById(101);
        assertEquals(item, result);
    }
}
