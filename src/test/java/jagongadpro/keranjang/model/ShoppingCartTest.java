// package jagongadpro.keranjang.model;

// import org.junit.jupiter.api.Test;
// import static org.junit.jupiter.api.Assertions.assertEquals;
// import static org.junit.jupiter.api.Assertions.assertTrue;

// public class ShoppingCartTest {

//     @Test
//     public void testAddItemToCart() {
//         ShoppingCart cart = new ShoppingCart("user@example.com");
//         int itemId = 101;
//         int quantity = 5;
//         cart.getItems().put(itemId, quantity);
        
//         assertTrue(cart.getItems().containsKey(itemId));
//         assertEquals(5, cart.getItems().get(itemId));
//     }

//     @Test
//     public void testUpdateItemQuantity() {
//         ShoppingCart cart = new ShoppingCart("user@example.com");
//         int itemId = 101;
//         int initialQuantity = 5;
//         cart.getItems().put(itemId, initialQuantity);

//         // Update the quantity
//         int updatedQuantity = 10;
//         cart.getItems().put(itemId, updatedQuantity);
        
//         assertEquals(updatedQuantity, cart.getItems().get(itemId));
//     }

//     @Test
//     public void testClearItems() {
//         ShoppingCart cart = new ShoppingCart("user@example.com");
//         cart.getItems().put(101, 5);
//         cart.getItems().put(102, 3);
        
//         cart.getItems().clear();  

//         assertTrue(cart.getItems().isEmpty());
//     }
// }
